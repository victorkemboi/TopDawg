package com.mes.topdawg.android.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

val maroon200 = Color(0xFFb73d2a)
val maroon500 = Color(0xFF800000)
val maroon700 = Color(0xFF4f0000)
val teal200 = Color(0xFF03DAC5)

val lowAvailabilityColor = Color(0xFFFF8C00)
val highAvailabilityColor = Color(0xFF008000)

val orange200 = Color(0xFFFFAB91)
val orange500 = Color(0xFFFF5722)
val orange700 = Color(0xFFE64A19)
val orange900 = Color(0xFFBF360C)

val purple200 = Color(0xFFB39DDB)
val purple500 = Color(0xFF673AB7)
val purple700 = Color(0xFF512DA8)

val yellow200 = Color(0xFFFFF59D)
val pink200 = Color(0xFFF48FB1)
val red200 = Color(0xFFEF9A9A)
val green200 = Color(0xFFC5E1A5)

fun getRandomColor() = listOf(
    maroon200, maroon500, maroon700, teal200,
    orange200, orange500, orange700,
    purple200, purple500, purple700
).random()

fun getRandomLightColor() = listOf(
    teal200,
    orange200,
    purple200,
    yellow200,
    pink200,
    red200,
    green200
).random()

fun Color.darkenColor() =
    Color(ColorUtils.blendARGB(this.toArgb(), Color.Black.toArgb(), 0.5f))

fun Color.lightenColor() = Color(ColorUtils.blendARGB(this.toArgb(), Color.White.toArgb(), 0.5f))
