package com.prieto.cafecampus.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.prieto.cafecampus.R
import com.prieto.cafecampus.data.local.entity.Producto
import java.io.File

@Composable
fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit,
    onAgregarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val esUriGaleria = remember(producto.icono) {
        producto.icono.startsWith("content://") ||
                producto.icono.startsWith("file://") ||
                producto.icono.startsWith("/") ||
                producto.icono.contains("/")
    }

    val modeloImagen = remember(producto.icono) {
        if (producto.icono.startsWith("/")) {
            File(producto.icono)
        } else {
            producto.icono
        }
    }

    val imageResId = remember(producto.id, producto.nombre, producto.icono) {
        if (esUriGaleria) return@remember 0

        var resId = 0

        resId = when (producto.nombre.lowercase().trim()) {
            "café americano", "cafe americano" -> R.drawable.cafe_americano
            "sándwich de pollo", "sandwich de pollo" -> R.drawable.sandwich_de_pollo
            "menú del día", "menu del dia" -> R.drawable.plato_menu
            "jugo de naranja" -> R.drawable.jugo_naranja
            "empanada" -> R.drawable.empanada_peruana
            "chicha morada" -> R.drawable.chicha_morada
            else -> 0
        }

        if (resId == 0) {
            val nombreLimpio = producto.nombre.lowercase()
                .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                .replace(" ", "_")
            resId = context.resources.getIdentifier(nombreLimpio, "drawable", context.packageName)
        }

        if (resId == 0 && producto.icono.isNotBlank() && producto.icono != "fastfood") {
            val nombreIcono = producto.icono.substringBeforeLast(".")
            resId = context.resources.getIdentifier(nombreIcono, "drawable", context.packageName)
        }

        if (resId == 0) R.drawable.plato_menu else resId
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                when {
                    esUriGaleria -> {
                        AsyncImage(
                            model = modeloImagen,
                            contentDescription = producto.nombre,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    imageResId != 0 -> {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = producto.nombre,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Text(
                            text = "☕",
                            fontSize = 32.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.nombre,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "S/ %.2f".format(producto.precio),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(
                    onClick = onAgregarClick,
                    enabled = producto.disponible,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = if (producto.disponible) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar al pedido",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}