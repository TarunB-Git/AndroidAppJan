package com.example.janapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.LocalLayoutDirection
import com.example.janapps.ui.theme.JanAppsTheme
import com.example.janapps.ui.theme.SwitchScreen


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JanAppsTheme {
                val layoutDirection = LocalLayoutDirection.current
                val windowSize = calculateWindowSizeClass(this)
                SwitchScreen(
                    windowSize = windowSize.widthSizeClass
                )
            }
        }
    }
}

