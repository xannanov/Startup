package com.xannanov.startup.base

import kotlinx.coroutines.flow.MutableSharedFlow

sealed interface GlobalEvents {
}

object EventBus {
    val globalEventBus = MutableSharedFlow<GlobalEvents>()
}