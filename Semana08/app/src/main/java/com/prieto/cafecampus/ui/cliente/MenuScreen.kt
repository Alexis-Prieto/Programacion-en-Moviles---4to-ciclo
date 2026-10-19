package com.prieto.cafecampus.ui.cliente

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    vm: MenuViewModel,
    onProducto: (Int) -> Unit,
    onCarrito: () -> Unit = {},
    onPedidos: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    val productos by vm.productos.collectAsState()
    val carrito by vm.carrito.collectAsState()
    val totalItemsCarrito = carrito.sumOf { it.cantidad }

    var busqueda by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todo") }

    val categorias = listOf("Todo", "Desayunos", "Almuerzos", "Bebidas")
    val colorNaranjaPrimario = Color(0xFFEA580C)
    val colorNaranjaContenedor = Color(0xFFFFEDD5)
    val colorFondo = Color(0xFFFAFAFA)

    val productosFiltrados = productos.filter { p ->
        val coincideBusqueda = p.nombre.contains(busqueda, ignoreCase = true)
        val coincideCategoria = if (categoriaSeleccionada == "Todo") true
        else p.categoria.equals(categoriaSeleccionada, ignoreCase = true)
        coincideBusqueda && coincideCategoria
    }
    Scaffold(
        containerColor = colorFondo,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = "Menú") },
                    label = { Text("Menú") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorNaranjaPrimario,
                        selectedTextColor = colorNaranjaPrimario,
                        indicatorColor = colorNaranjaContenedor
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onPedidos,
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Pedidos") },
                    label = { Text("Pedidos") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onPerfil,
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Hola, Alexis!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "¿Qué se te antoja hoy?",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
                BadgedBox(
                    badge = {
                        if (totalItemsCarrito > 0) {
                            Badge(
                                containerColor = colorNaranjaPrimario,
                                contentColor = Color.White
                            ) {
                                Text(text = totalItemsCarrito.toString())
                            }
                        }
                    }
                ) {
                    IconButton(onClick = onCarrito) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Carrito",
                            tint = Color(0xFF1F2937),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar en el menú...", color = Color(0xFF9CA3AF)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorNaranjaPrimario,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias) { cat ->
                    FilterChip(
                        selected = categoriaSeleccionada == cat,
                        onClick = { categoriaSeleccionada = cat },
                        label = { Text(cat, fontSize = 13.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colorNaranjaPrimario,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF4B5563)
                        ),
                        border = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Populares hoy",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(12.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(productosFiltrados) { p ->
                    ProductoCard(p) { onProducto(p.id) }
                }
            }
        }
    }
}

@Composable
private fun ProductoCard(
    p: Producto,
    onClick: () -> Unit
) {
    val colorNaranjaPrimario = Color(0xFFEA580C)
    val colorNaranjaContenedor = Color(0xFFFFEDD5)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(colorNaranjaContenedor, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                RenderizarMiniaturaProducto(
                    iconoNombre = p.icono,
                    modifier = Modifier.size(52.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = p.nombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "S/ %.2f".format(p.precio),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorNaranjaPrimario
                )
                IconButton(
                    onClick = onClick,
                    enabled = p.disponible,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            if (p.disponible) colorNaranjaPrimario else Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
@Composable
private fun RenderizarMiniaturaProducto(
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