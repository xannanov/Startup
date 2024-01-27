package com.xannanov.startup.main

import com.xannanov.startup.base.CardSwipeBase

data class MainScreenViewState(
    val cardsItems: List<CardItem>
) {

    val trimmedItems
        get() = cardsItems.takeLast(5)

    fun toSkipLast() = copy(
        cardsItems = cardsItems
            .filterIndexed { index, _ -> index != cardsItems.lastIndex }
    )
}

data class CardItem(
    val title: String,
) : CardSwipeBase()
