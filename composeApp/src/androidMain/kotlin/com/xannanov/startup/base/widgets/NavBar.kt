package com.xannanov.startup.base.widgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.NAV_BAR_HEIGHT),
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
                contentDescription = null,
            )
        }
    }
}