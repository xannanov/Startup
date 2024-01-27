package com.xannanov.startup.base

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChangeIgnoreConsumed
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

// region Card Swipe
abstract class CardSwipeBase(
    val uuid: String = UUID.randomUUID().toString(),
    // TODO: ВРЕМЕННОЕ РЕШЕНИЕ. Попробовать выделить в State для модифайра
    var offset: Float = CARD_BASE_OFFSET * 5f,
)

fun Modifier.cardSwipe(
    item: CardSwipeBase,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
): Modifier = composed {
    val offsetX = remember(item.uuid) { Animatable(0f) }
    pointerInput(item.uuid) {
        val decay = splineBasedDecay<Float>(this)

        coroutineScope {
            while (true) {
                val velocityTracker = VelocityTracker()
                offsetX.stop()
                awaitPointerEventScope {
                    val pointerId = awaitFirstDown().id

                    horizontalDrag(pointerId) { change ->
                        launch {
                            offsetX.snapTo(
                                targetValue = offsetX.value +
                                        change.positionChangeIgnoreConsumed().x
                            )
                        }

                        velocityTracker.addPosition(
                            timeMillis = change.uptimeMillis,
                            position = change.position
                        )
                    }
                }

                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)

                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat(),
                )

                launch {
                    if (
                        targetOffsetX.absoluteValue <= size.width &&
                        offsetX.value.absoluteValue < size.width / 2
                    ) {
                        offsetX.animateTo(
                            targetValue = 0f,
                            initialVelocity = velocity,
                        )
                    } else {
                        val isDismissed = offsetX.value > 0
                        offsetX.animateTo(
                            targetValue = if (isDismissed) {
                                size.width.toFloat()
                            } else {
                                -size.width.toFloat()
                            },
                            initialVelocity = velocity,
                        )
                        if (isDismissed) {
                            onDismiss.invoke()
                        } else {
                            onApply.invoke()
                        }
                    }
                }
            }
        }
    }
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}

private const val CARD_BASE_OFFSET = 16f
fun Modifier.cardOffset(
    items: List<CardSwipeBase>,
    item: CardSwipeBase,
    index: Int,
): Modifier = composed {
    val offset = remember(items) { Animatable(item.offset) }

    LaunchedEffect(items) {
        if (items.lastIndex == index) {
            offset.animateTo(0f)
            item.offset = 0f
        } else {
            offset.animateTo((items.lastIndex - index) * CARD_BASE_OFFSET)
            item.offset = (items.lastIndex - index) * CARD_BASE_OFFSET
        }
    }
    offset { IntOffset(-offset.value.roundToInt(), offset.value.roundToInt()) }
}
// endregion Card Swipe

