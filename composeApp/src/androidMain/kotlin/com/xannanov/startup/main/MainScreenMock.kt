package com.xannanov.startup.main

import kotlinx.coroutines.flow.MutableStateFlow

val testMainScreenViewState = MainScreenViewState(
    cardsItems = List(8) { CardItem(title = it.toString()) }
)

var testMainViewStateFlow = MutableStateFlow(testMainScreenViewState)