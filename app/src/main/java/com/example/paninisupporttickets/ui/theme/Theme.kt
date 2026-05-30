package com.example.paninisupporttickets.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PaniniBlue80,
    secondary = PaniniBlueGrey80,
    tertiary = PaniniRed80
)

private val LightColorScheme = lightColorScheme(
    primary = PaniniBlue40,
    secondary = PaniniBlueGrey40,
    tertiary = PaniniRed40
)

@Composable
fun PaniniSupportTicketsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}