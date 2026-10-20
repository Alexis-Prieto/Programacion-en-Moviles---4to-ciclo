package com.prieto.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EstadoChip(
    estado: String,
    modifier: Modifier = Modifier
) {
    val (colorFondo, colorTexto) = when (estado.lowercase()) {
        "pendiente" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "en preparación", "preparando" -> Color(0xFFDBEAFE) to Color(0xFF2563EB)
        "listo", "completado", "entregado" -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
        "cancelado" -> Color(0xFFFEE2E2) to Color(0xFFDC2626)
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .background(color = colorFondo, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = estado,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )
    }
}