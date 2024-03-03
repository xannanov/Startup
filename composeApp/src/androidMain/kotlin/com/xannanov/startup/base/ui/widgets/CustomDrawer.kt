package com.xannanov.startup.base.ui.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch

@Composable
fun CustomDrawer(
    menuContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val translationX = remember { Animatable(0f) }
    val tonalElevation = remember { Animatable(0f) }
    val isDrawerExpanded = remember { mutableStateOf(false) }
    val draggableState = rememberDraggableState { dragAmount ->
        coroutineScope.launch {
            translationX.snapTo(translationX.value + dragAmount)
        }
    }
    val decay = rememberSplineBasedDecay<Float>()
    val drawerWidth = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isDrawerExpanded.value) {
        if (isDrawerExpanded.value) {
            tonalElevation.animateTo(12f)
        } else {
            tonalElevation.animateTo(0f)
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        drawerWidth.floatValue = with(LocalDensity.current) { maxWidth.toPx() }
        translationX.updateBounds(0f, drawerWidth.floatValue * .8f)

        Surface(
            color = MaterialTheme.colorScheme.surface,
        ) {
            menuContent()
        }

        Surface(
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = translationX.value

                    val scaleCoeff = lerp(1f, .8f, translationX.value / drawerWidth.floatValue)
                    this.scaleX = scaleCoeff
                    this.scaleY = scaleCoeff
                }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = { velocity: Float ->
                        val decayX = decay.calculateTargetValue(translationX.value, velocity)

                        coroutineScope.launch {
                            val targetX = if (decayX > drawerWidth.floatValue * .5f) {
                                isDrawerExpanded.value = true
                                drawerWidth.floatValue
                            } else {
                                isDrawerExpanded.value = false
                                0f
                            }

                            val canReachTargetWithDecay =
                                (targetX == drawerWidth.floatValue && decayX > targetX) ||
                                        (targetX == 0f && decayX < targetX)

                            if (canReachTargetWithDecay) {
                                translationX.animateDecay(
                                    initialVelocity = velocity,
                                    animationSpec = decay
                                )
                            } else {
                                translationX.animateTo(
                                    targetValue = targetX,
                                    initialVelocity = velocity,
                                )
                            }
                        }
                    }
                )
                .then(
                    if (translationX.value != 0f) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            coroutineScope.launch {
                                isDrawerExpanded.value = false
                                translationX.animateTo(0f)
                            }
                        }
                    } else Modifier
                )
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize(),
            tonalElevation = tonalElevation.value.dp,
            shadowElevation = 12.dp,
        ) {
            content()
        }
    }
}

@Composable
fun DefaultDrawerMenu() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        DefaultDrawerMenuItem("Профиль") {}
        DefaultDrawerMenuItem("Акции") {}
        DefaultDrawerMenuItem("Выйти") {}
    }
}

@Composable
fun DefaultDrawerMenuItem(
    itemText: String,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onItemClick() }
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = itemText,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
