package com.prieto.cafecampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun ProductoIcono(
    iconoNombre: String,
    modifier: Modifier = Modifier
) {
    when {
        iconoNombre.contains("plato", ignoreCase = true) || iconoNombre.contains("almuerzo", ignoreCase = true) -> {
            IconoPlatoComida(modifier = modifier)
        }
        iconoNombre.contains("bebida", ignoreCase = true) || iconoNombre.contains("jugo", ignoreCase = true) -> {
            IconoVasoBebida(modifier = modifier)
        }
        else -> {
            IconoTazaCafe(modifier = modifier)
        }
    }
}

@Composable
private fun IconoTazaCafe(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.08f, h * 0.72f),
            size = Size(w * 0.84f, h * 0.09f),
            cornerRadius = CornerRadius(h * 0.04f, h * 0.04f)
        )
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.18f, h * 0.25f),
            size = Size(w * 0.52f, h * 0.44f),
            cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
        )
        drawRoundRect(
            color = Color(0xFF5A3825),
            topLeft = Offset(w * 0.22f, h * 0.29f),
            size = Size(w * 0.44f, h * 0.14f),
            cornerRadius = CornerRadius(w * 0.05f, w * 0.05f)
        )
        drawArc(
            color = Color.White,
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.62f, h * 0.33f),
            size = Size(w * 0.22f, h * 0.28f),
            style = Stroke(width = w * 0.08f)
        )
    }
}

@Composable
private fun IconoPlatoComida(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val center = Offset(w / 2f, h / 2f)

        drawCircle(
            color = Color.White,
            radius = w * 0.40f,
            center = center,
            style = Stroke(width = w * 0.13f)
        )
        val r = w * 0.08f
        drawCircle(
            color = Color(0xFFDC2626),
            radius = r,
            center = Offset(center.x, center.y - r * 0.95f)
        )
        drawCircle(
            color = Color(0xFFF59E0B),
            radius = r,
            center = Offset(center.x - r * 0.9f, center.y + r * 0.65f)
        )
        drawCircle(
            color = Color(0xFF16A34A),
            radius = r,
            center = Offset(center.x + r * 0.9f, center.y + r * 0.65f)
        )
    }
}

@Composable
private fun IconoVasoBebida(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val strawPath = Path().apply {
            moveTo(w * 0.68f, h * 0.08f)
            lineTo(w * 0.52f, h * 0.42f)
        }
        drawPath(
            path = strawPath,
            color = Color(0xFF16A34A),
            style = Stroke(width = w * 0.07f, cap = StrokeCap.Round)
        )
        val glassTopPath = Path().apply {
            moveTo(w * 0.22f, h * 0.30f)
            lineTo(w * 0.78f, h * 0.30f)
            lineTo(w * 0.73f, h * 0.56f)
            lineTo(w * 0.27f, h * 0.56f)
            close()
        }
        drawPath(path = glassTopPath, color = Color.White)

        val liquidPath = Path().apply {
            moveTo(w * 0.27f, h * 0.56f)
            lineTo(w * 0.73f, h * 0.56f)
            lineTo(w * 0.68f, h * 0.88f)
            lineTo(w * 0.32f, h * 0.88f)
            close()
        }
        drawPath(path = liquidPath, color = Color(0xFFEA580C))

        val outlinePath = Path().apply {
            moveTo(w * 0.22f, h * 0.30f)
            lineTo(w * 0.78f, h * 0.30f)
            lineTo(w * 0.68f, h * 0.88f)
            lineTo(w * 0.32f, h * 0.88f)
            close()
        }
        drawPath(
            path = outlinePath,
            color = Color.White,
            style = Stroke(width = w * 0.04f)
        )
    }
}