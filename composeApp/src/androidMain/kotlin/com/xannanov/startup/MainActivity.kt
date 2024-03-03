package com.xannanov.startup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.xannanov.startup.base.ui.StartupTheme
import com.xannanov.startup.base.ui.widgets.CustomDrawer
import com.xannanov.startup.base.ui.widgets.DefaultDrawerMenu
import com.xannanov.startup.main.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StartupTheme {
                CustomDrawer(
                    menuContent = { DefaultDrawerMenu() },
                    content = { MainScreen() },
                )
            }
        }
    }
}
