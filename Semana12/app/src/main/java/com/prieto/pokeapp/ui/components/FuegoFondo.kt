package com.prieto.pokeapp.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

/** Paisaje decorativo compartido; no intercepta los controles de la pantalla. */
fun Modifier.fuegoFondo(): Modifier = drawBehind {
    drawRect(brush = Brush.verticalGradient(listOf(Color(0xFFFFF9F1), Color(0xFFFFEBD5))))
    val y = size.height * 0.83f
    drawCircle(Color(0xFFFFB886).copy(alpha = 0.55f), size.minDimension * 0.16f, Offset(size.width * 0.72f, y))
    val mountain = Path().apply {
        moveTo(0f, size.height)
        lineTo(0f, size.height * 0.94f)
        lineTo(size.width * 0.18f, size.height * 0.87f)
        lineTo(size.width * 0.31f, size.height * 0.91f)
        lineTo(size.width * 0.52f, size.height * 0.82f)
        lineTo(size.width * 0.77f, size.height * 0.91f)
        lineTo(size.width, size.height * 0.84f)
        lineTo(size.width, size.height)
        close()
    }
    drawPath(mountain, Color(0xFFEF855D).copy(alpha = 0.38f))
}
