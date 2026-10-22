package com.prieto.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coil.compose.AsyncImage
import com.prieto.cafecampus.R
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.ui.components.AppTopBar
import com.prieto.cafecampus.viewmodel.MenuAdminViewModel

@Composable
fun MenuAdminScreen(
    menuAdminViewModel: MenuAdminViewModel,
    onVolver: () -> Unit,
    onNuevoProducto: () -> Unit,
    onEditarProducto: (Int) -> Unit,
    onNavigatePanel: () -> Unit,
    onNavigatePedidos: () -> Unit,
    onNavigateAjustes: () -> Unit
) {
    val categoriaSeleccionada by menuAdminViewModel.categoriaSeleccionada.collectAsState()
    val productosList by menuAdminViewModel.productosFiltrados.collectAsState(initial = emptyList())

    val colorPurpuraAdmin = Color(0xFF4F46E5)
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    val categorias = listOf("Todo", "Desayunos", "Almuerzos", "Bebidas")

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = "Gestión del menú",
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoProducto,
                containerColor = colorPurpuraAdmin,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Producto")
            }
        },
        bottomBar = {
            BarraNavegacionAdmin(
                pestanaActual = "Menú",
                onNavigatePanel = onNavigatePanel,
                onNavigatePedidos = onNavigatePedidos,
                onNavigateMenu = { },
                onNavigateAjustes = onNavigateAjustes
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categorias) { cat ->
                    val seleccionado = categoriaSeleccionada.equals(cat, ignoreCase = true)
                    FilterChip(
                        selected = seleccionado,
                        onClick = { menuAdminViewModel.cambiarCategoria(cat) },
                        label = { Text(cat, fontSize = 13.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colorPurpuraAdmin,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (productosList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos en esta categoría",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(productosList) { prod ->
                        TarjetaProductoAdmin(
                            producto = prod,
                            colorPurpuraAdmin = colorPurpuraAdmin,
                            onToggleDisponibilidad = { menuAdminViewModel.alternarDisponibilidad(prod) },
                            onEditar = { onEditarProducto(prod.id) },
                            onEliminar = { productoAEliminar = prod }
                        )
                    }
                }
            }
        }
    }
    productoAEliminar?.let { prod ->
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = {
                Text(
                    text = "Eliminar producto",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar \"${prod.nombre}\" del menú?",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        menuAdminViewModel.eliminarProducto(prod)
                        productoAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { productoAEliminar = null }) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}
@Composable
private fun TarjetaProductoAdmin(
    producto: Producto,
    colorPurpuraAdmin: Color,
    onToggleDisponibilidad: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val context = LocalContext.current
    val esOscuro = isSystemInDarkTheme()

    val esUriGaleria = remember(producto.icono) {
        producto.icono.startsWith("content://") ||
                producto.icono.startsWith("file://") ||
                producto.icono.contains("/")
    }

    val imagenDrawableRes = remember(producto.id, producto.nombre, producto.icono) {
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

    val fondoBadge = if (producto.disponible) {
        if (esOscuro) Color(0xFF14532D) else Color(0xFFDCFCE7)
    } else {
        if (esOscuro) Color(0xFF7F1D1D) else Color(0xFFFEE2E2)
    }
    val textoBadge = if (producto.disponible) {
        if (esOscuro) Color(0xFF4ADE80) else Color(0xFF15803D)
    } else {
        if (esOscuro) Color(0xFFFCA5A5) else Color(0xFFB91C1C)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                when {
                    esUriGaleria -> {
                        AsyncImage(
                            model = producto.icono,
                            contentDescription = producto.nombre,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    imagenDrawableRes != 0 -> {
                        Image(
                            painter = painterResource(id = imagenDrawableRes),
                            contentDescription = producto.nombre,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${producto.categoria} · S/ %.2f".format(producto.precio),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = fondoBadge,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onToggleDisponibilidad() }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (producto.disponible) "Disponible" else "Agotado",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textoBadge
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEditar, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = colorPurpuraAdmin,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onEliminar, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}