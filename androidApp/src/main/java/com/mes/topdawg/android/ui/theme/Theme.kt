package com.mes.topdawg.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = maroon200,
    primaryVariant = orange900,
    secondary = yellow200
)

private val LightColorPalette = lightColors(
    primary = maroon200,
    primaryVariant = orange900,
    secondary = yellow200
)

@Composable
fun TopDawgTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
