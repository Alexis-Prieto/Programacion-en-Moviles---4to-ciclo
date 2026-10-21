package com.prieto.cafecampus.ui.cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.cafecampus.ui.components.AppTopBar
import com.prieto.cafecampus.ui.components.CafeButton
import com.prieto.cafecampus.ui.components.CafeTextField
import com.prieto.cafecampus.ui.components.ProductoIcono
import com.prieto.cafecampus.viewmodel.MenuViewModel

@Composable
fun DetalleProductoScreen(
    productoId: Int,
    menuViewModel: MenuViewModel,
    onVolver: () -> Unit,
    onProductoAgregado: () -> Unit
) {
    val context = LocalContext.current

    val productos by menuViewModel.productos.collectAsState()
    val producto = remember(productos, productoId) {
        productos.find { it.id == productoId }
    }

    var cantidad by remember { mutableIntStateOf(1) }
    var nota by remember { mutableStateOf("") }

    if (producto == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Producto no encontrado",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val imageResId = remember(producto.imagenRes, producto.icono) {
        if (producto.imagenRes != 0) {
            producto.imagenRes
        } else {
            val nombreLimpio = producto.icono.substringBeforeLast(".")
            val id = context.resources.getIdentifier(
                nombreLimpio,
                "drawable",
                context.packageName
            )
            if (id != 0) id else 0
        }
    }

    val totalCalculado = producto.precio * cantidad

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = "Detalle del producto",
                onBack = onVolver
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.background
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 12.dp)
                ) {
                    CafeButton(
                        texto = if (producto.disponible) "Agregar al pedido" else "Producto Agotado",
                        onClick = {
                            menuViewModel.agregar(producto, cantidad, nota)
                            onProductoAgregado()
                        },
                        enabled = producto.disponible
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = producto.nombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    ProductoIcono(
                        iconoNombre = producto.icono,
                        modifier = Modifier.size(110.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = producto.nombre,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "S/ %.2f".format(producto.precio),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (producto.disponible) {
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Disponible", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFFDCFCE7),
                            labelColor = Color(0xFF15803D)
                        ),
                        border = null
                    )
                } else {
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Agotado", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFFFEE2E2),
                            labelColor = Color(0xFFB91C1C)
                        ),
                        border = null
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "10 – 15 min",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = producto.descripcion,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cantidad",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (cantidad > 1) cantidad-- },
                        enabled = producto.disponible && cantidad > 1,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = if (cantidad > 1) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Restar",
                            tint = if (cantidad > 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = cantidad.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    IconButton(
                        onClick = { cantidad++ },
                        enabled = producto.disponible,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = if (producto.disponible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Sumar",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Nota para la cocina",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            CafeTextField(
                valor = nota,
                onValorChange = { nota = it },
                placeholder = ""
            )

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = "Total",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "S/ %.2f".format(totalCalculado),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}