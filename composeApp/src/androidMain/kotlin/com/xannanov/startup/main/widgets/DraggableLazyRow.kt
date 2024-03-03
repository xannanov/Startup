package com.xannanov.startup.main.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val items = List(10) { it }

@Composable
fun DraggableLazyRow() {
    val dragAndDropState = rememberLazyDragAndDropState()

    LazyColumn(
        state = dragAndDropState.getLazyState(),
        modifier = Modifier.lazyDragAndDrop(dragAndDropState)
    ) {
        itemsIndexed(items) { index, item ->
            val offset = if (index == dragAndDropState.getCurrentDraggableIndex())
                dragAndDropState.getCurrentDraggableItemOffset()
            else null

            Card(
                modifier = Modifier
                    .then(
                        if (offset != null) {
                            Modifier.graphicsLayer {
                                this.translationX = offset.x
                                this.translationY = offset.y
                            }
                        } else Modifier
                    )
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(text = "$item")
            }
        }
    }
}

@Composable
fun rememberLazyDragAndDropState(
    lazyListState: LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) = remember { LazyDragAndDropStateImpl(state = lazyListState, coroutineScope = coroutineScope) }

interface LazyDragAndDropState {
    fun onDragStart(hitPoint: Offset)
    fun onDragEnd()
    fun onDragInterrupt()
    fun onDrag(offset: Offset)
    fun getLazyState(): LazyListState
    fun getCurrentDraggableItemOffset(): Offset
    fun getCurrentDraggableIndex(): Int
}

class LazyDragAndDropStateImpl(
    private val state: LazyListState,
    private val coroutineScope: CoroutineScope,
) : LazyDragAndDropState {
    private val currentItemIndex: MutableState<Int> = mutableIntStateOf(-1)
    private val offsetX = Animatable(0f)
    private val offsetY = Animatable(0f)

    override fun getLazyState(): LazyListState = state

    override fun onDragStart(hitPoint: Offset) {
        state.layoutInfo.visibleItemsInfo.firstOrNull { item ->
            hitPoint.y.toInt() in item.offset..(item.offset + item.size)
        }?.also {
            currentItemIndex.value = it.index
        }
    }

    override fun onDragEnd() {
        animateToInitialPosition()
    }

    override fun onDragInterrupt() {
        animateToInitialPosition()
    }

    override fun onDrag(offset: Offset) {
        launchInternal { offsetY.snapTo(offsetY.value + offset.y) }
        launchInternal { offsetX.snapTo(offsetX.value + offset.x) }
    }

    override fun getCurrentDraggableItemOffset(): Offset = Offset(offsetX.value, offsetY.value)
    override fun getCurrentDraggableIndex(): Int = currentItemIndex.value

    private fun animateToInitialPosition() {
        launchInternal { offsetY.animateTo(0f) }
        launchInternal { offsetX.animateTo(0f) }
    }

    private fun LazyDragAndDropStateImpl.launchInternal(block: suspend () -> Unit) {
        coroutineScope.launch { block() }
    }
}

fun Modifier.lazyDragAndDrop(
    state: LazyDragAndDropState,
): Modifier = this.composed {
    pointerInput(Unit) {
        detectDragGesturesAfterLongPress(
            onDragStart = { offset ->
                state.onDragStart(offset)
            },
            onDragEnd = {
                state.onDragEnd()
            },
            onDragCancel = {
                state.onDragInterrupt()
            },
            onDrag = { change, dragAmount ->
                state.onDrag(dragAmount)
            },
        )
    }
}
