package com.xannanov.startup.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.xannanov.startup.R
import com.xannanov.startup.base.ui.widgets.NavBar
import com.xannanov.startup.main.widgets.CardBlock
import com.xannanov.startup.main.widgets.DraggableLazyRow
import kotlinx.coroutines.flow.update

@Composable
fun MainScreen() {
    val viewState by testMainViewStateFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        NavBar(
            titleRes = R.string.app_name,
//            leftIconRes = R.drawable.ic_back,
//            rightIconRes = R.drawable.ic_apps,
            leftIconClick = {},
            rightIconClick = {},
        )

        CardBlock(
            cardItems = viewState.trimmedItems,
            onDismiss = {
                testMainViewStateFlow.update {
                    it.toSkipLast()
                }
            },
            onApply = {
                testMainViewStateFlow.update {
                    it.toSkipLast()
                }
            },
        )

        DraggableLazyRow()
    }
}