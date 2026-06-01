package com.example.habittracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.habittracker.data.ThemeMode

private val Seed = Color(0xFF1D9E75)

private val LightColors = lightColorScheme(
    primary = Seed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB6F2DA),
    onPrimaryContainer = Color(0xFF00251A),
    background = Color(0xFFFBFDF9),
    surface = Color(0xFFFBFDF9),
    surfaceVariant = Color(0xFFDBE5DF),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF5DCAA5),
    onPrimary = Color(0xFF003828),
    primaryContainer = Color(0xFF005139),
    onPrimaryContainer = Color(0xFFB6F2DA),
    background = Color(0xFF101512),
    surface = Color(0xFF101512),
    surfaceVariant = Color(0xFF3F4944),
)

@Composable
fun HabitTrackerTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        content = content
    )
}
