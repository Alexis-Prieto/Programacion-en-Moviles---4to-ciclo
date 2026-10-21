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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.ui.components.AppTopBar
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.PedidosClienteViewModel

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
            AppTopBar(
                titulo = "Mis pedidos",
                onBack = onVolver
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateMenu,
                    icon = { Icon(Icons.Default.Home, contentDescription = "Menú") },
                    label = { Text("Menú") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Pedidos") },
                    label = { Text("Pedidos") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigatePerfil,
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(4.dp)
            ) {
                listOf("Activos", "Historial").forEach { pestaña ->
                    val seleccionada = pestañaSeleccionada == pestaña
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .background(
                                color = if (seleccionada) MaterialTheme.colorScheme.surface else Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { pestañaSeleccionada = pestaña },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pestaña,
                            fontSize = 13.sp,
                            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                            color = if (seleccionada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(pedidosFiltrados) { index, pedConDetalle ->
                        val numeroPersonal = misPedidos.size - misPedidos.indexOf(pedConDetalle)
                        val numeroFormateado = "Pedido #%04d".format(numeroPersonal)

                        TarjetaPedido(
                            pedConDetalle = pedConDetalle,
                            numeroFormateado = numeroFormateado,
                            menuViewModel = menuViewModel,
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
    numeroFormateado: String,
    menuViewModel: MenuViewModel,
    onClick: () -> Unit
) {
    val ped = pedConDetalle.pedido
    val detalles = pedConDetalle.detalles

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = numeroFormateado,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Hoy ${ped.horaRecojo}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EstadoPedidoBadge(estado = ped.estado.toString())

                Text(
                    text = "S/ %.2f".format(ped.total),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun EstadoPedidoBadge(
    estado: String,
    modifier: Modifier = Modifier
) {
    val (fondoColor, textoColor, textoFormateado) = when (estado.uppercase()) {
        "RECIBIDO", "PREPARANDO" -> Triple(
            Color(0xFFFEF3C7),
            Color(0xFFD97706),
            "Preparando"
        )
        "LISTO" -> Triple(
            Color(0xFFDCFCE7),
            Color(0xFF15803D),
            "Listo"
        )
        "ENTREGADO" -> Triple(
            Color(0xFFE5E7EB),
            Color(0xFF6B7280),
            "Entregado"
        )
        "CANCELADO" -> Triple(
            Color(0xFFFEE2E2),
            Color(0xFFDC2626),
            "Cancelado"
        )
        else -> Triple(
            Color(0xFFE5E7EB),
            Color(0xFF6B7280),
            estado.lowercase().replaceFirstChar { it.uppercase() }
        )
    }

    Box(
        modifier = modifier
            .background(color = fondoColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textoFormateado,
            color = textoColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}