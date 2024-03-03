package com.xannanov.startup.main.widgets

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xannanov.startup.base.cardOffset
import com.xannanov.startup.base.cardSwipe
import com.xannanov.startup.main.CardItem

@Composable
fun CardBlock(
    cardItems: List<CardItem>,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
    ) {
        cardItems.forEachIndexed { index, item ->
            Box(
                modifier = Modifier
                    .cardOffset(
                        items = cardItems,
                        item = item,
                        index = index,
                    )
                    .cardSwipe(
                        item = item,
                        onDismiss = {
                            onDismiss.invoke()
                            Toast
                                .makeText(context, "dismiss", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onApply = {
                            onApply.invoke()
                            Toast
                                .makeText(context, "apply", Toast.LENGTH_SHORT)
                                .show()
                        },
                    )
                    .padding(12.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (index == cardItems.lastIndex) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.tertiaryContainer
                        }
                    )
                    .then(
                        if (index == cardItems.lastIndex) {
                            Modifier
                        } else {
                            Modifier.border(
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onTertiary),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item.title,
                    color = if (index == cardItems.lastIndex) {
                        MaterialTheme.colorScheme.onTertiary
                    } else {
                        MaterialTheme.colorScheme.onTertiaryContainer
                    },
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}