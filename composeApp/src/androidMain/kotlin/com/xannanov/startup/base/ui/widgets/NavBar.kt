package com.xannanov.startup.base.ui.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xannanov.startup.base.Dimensions

@Composable
fun NavBar(
    @StringRes titleRes: Int? = null,
    @DrawableRes leftIconRes: Int? = null,
    @DrawableRes rightIconRes: Int? = null,
    onTitleClick: (() -> Unit)? = null,
    leftIconClick: (() -> Unit)? = null,
    rightIconClick: (() -> Unit)? = null,
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.NAV_BAR_HEIGHT),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leftIconRes != null) {
                Icon(
                    modifier = Modifier
                        .then(
                            if (leftIconClick != null) Modifier.clickable { leftIconClick.invoke() }
                            else Modifier
                        ),
                    painter = painterResource(leftIconRes),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = null,
                )
            }

            if (titleRes != null) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .then(
                            if (onTitleClick != null) Modifier.clickable { onTitleClick.invoke() }
                            else Modifier
                        ),
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                )
            }

            if (rightIconRes != null) {
                Icon(
                    modifier = Modifier
                        .then(
                            if (rightIconClick != null) Modifier.clickable { rightIconClick.invoke() }
                            else Modifier
                        ),
                    painter = painterResource(rightIconRes),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = null,
                )
            }
        }
    }
}