package com.prieto.cafecampus.ui.cliente

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPedidosScreen(
    pedidosViewModel: PedidosClienteViewModel,
    menuViewModel: MenuViewModel,
    onVolver: () -> Unit,
    onPedidoClick: (Int) -> Unit,
    onNavigateMenu: () -> Unit,
    onNavigatePerfil: () -> Unit
) {
    val misPedidos by pedidosViewModel.misPedidos.collectAsState()
    var pestañaSeleccionada by remember { mutableStateOf("Activos") }

    val colorNaranjaPrimario = Color(0xFFEA580C)
    val colorNaranjaContenedor = Color(0xFFFFEDD5)
    val colorFondoPantalla = Color(0xFFFFFBF7)

    val estadosActivos = listOf("RECIBIDO", "PREPARANDO", "LISTO")
    val pedidosFiltrados = misPedidos.filter { ped ->
        val estadoStr = ped.pedido.estado.toString().uppercase()
        if (pestañaSeleccionada == "Activos") {
            estadoStr in estadosActivos
        } else {
            estadoStr !in estadosActivos
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis pedidos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF1F2937)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = colorFondoPantalla)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateMenu,
                    icon = { Icon(Icons.Default.LocalDining, contentDescription = "Menú") },
                    label = { Text("Menú") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Pedidos") },
                    label = { Text("Pedidos") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorNaranjaPrimario,
                        selectedTextColor = colorNaranjaPrimario,
                        indicatorColor = colorNaranjaContenedor
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigatePerfil,
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        },
        containerColor = colorFondoPantalla
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F4F6), shape = RoundedCornerShape(24.dp))
                    .padding(4.dp)
            ) {
                listOf("Activos", "Historial").forEach { pestaña ->
                    val seleccionada = pestañaSeleccionada == pestaña
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .background(
                                color = if (seleccionada) Color.White else Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { pestañaSeleccionada = pestaña },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pestaña,
                            fontSize = 13.sp,
                            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                            color = if (seleccionada) colorNaranjaPrimario else Color(0xFF6B7280)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (pedidosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (pestañaSeleccionada == "Activos") "No tienes pedidos activos" else "No hay historial de pedidos",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pedidosFiltrados) { pedConDetalle ->
                        TarjetaPedido(
                            pedConDetalle = pedConDetalle,
                            menuViewModel = menuViewModel,
                            colorNaranjaPrimario = colorNaranjaPrimario,
                            onClick = { onPedidoClick(pedConDetalle.pedido.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaPedido(
    pedConDetalle: PedidoConDetalle,
    menuViewModel: MenuViewModel,
    colorNaranjaPrimario: Color,
    onClick: () -> Unit
) {
    val ped = pedConDetalle.pedido
    val detalles = pedConDetalle.detalles

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #%04d".format(ped.id),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            val primerDetalle = detalles.firstOrNull()
            val primerNombre = primerDetalle?.let { menuViewModel.obtenerProductoPorId(it.productoId)?.nombre } ?: "Producto"
            val totalCantidad = detalles.sumOf { it.cantidad }
            val resumen = if (detalles.size > 1) "$primerNombre x${primerDetalle?.cantidad ?: 1} + ${detalles.size - 1} más" else "$primerNombre x$totalCantidad"

            Text(
                text = resumen,
                fontSize = 13.sp,
                color = Color(0xFF6B7280)
            )
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Hoy ${ped.horaRecojo}",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EstadoChip(estado = ped.estado.toString())

                Text(
                    text = "S/ %.2f".format(ped.total),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorNaranjaPrimario
                )
            }
        }
    }
}
@Composable
private fun EstadoChip(estado: String) {
    val (fondo, texto) = when (estado.uppercase()) {
        "RECIBIDO" -> Pair(Color(0xFFDBEAFE), Color(0xFF1D4ED8))
        "PREPARANDO" -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706))
        "LISTO" -> Pair(Color(0xFFDCFCE7), Color(0xFF15803D))
        "ENTREGADO" -> Pair(Color(0xFFF3F4F6), Color(0xFF4B5563))
        "CANCELADO" -> Pair(Color(0xFFFEE2E2), Color(0xFFB91C1C))
        else -> Pair(Color(0xFFF3F4F6), Color(0xFF4B5563))
    }

    SuggestionChip(
        onClick = { },
        label = {
            Text(
                text = estado.lowercase().replaceFirstChar { it.uppercase() },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = fondo,
            labelColor = texto
        ),
        border = null
    )
}