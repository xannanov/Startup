package com.xannanov.startup.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xannanov.startup.R
import com.xannanov.startup.base.ui.widgets.NavBar
import com.xannanov.startup.main.widgets.CardBlock
import kotlinx.coroutines.flow.update

@Composable
fun MainScreen() {
    val viewState by testMainViewStateFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
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
    }
}