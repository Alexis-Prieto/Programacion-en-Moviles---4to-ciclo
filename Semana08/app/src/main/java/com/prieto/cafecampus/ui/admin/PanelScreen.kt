package com.prieto.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.PanelAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelScreen(
    preferenciasStore: PreferenciasStore,
    panelViewModel: PanelAdminViewModel,
    menuViewModel: MenuViewModel,
    onNavigatePedidos: () -> Unit,
    onNavigateMenu: () -> Unit,
    onNavigateAjustes: () -> Unit
) {
    val esModoOscuro by preferenciasStore.modoOscuro.collectAsState(initial = false)

    val nuevos by panelViewModel.nuevos.collectAsState()
    val preparando by panelViewModel.preparando.collectAsState()
    val listos by panelViewModel.listos.collectAsState()
    val ventasHoy by panelViewModel.ventasHoy.collectAsState()
    val totalPedidosHoy by panelViewModel.totalPedidosHoy.collectAsState()
    val nuevosPedidos by panelViewModel.nuevosPedidosLista.collectAsState()
    val productosMenu by menuViewModel.productos.collectAsState()

    // Datos reales del gráfico de pedidos por hora
    val pedidosPorHora by panelViewModel.pedidosPorHora.collectAsState()

    val colorPurpuraAdmin = Color(0xFF4F46E5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Panel de control",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Cafetería · Hoy",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            BarraNavegacionAdmin(
                pestanaActual = "Panel",
                onNavigatePanel = { },
                onNavigatePedidos = onNavigatePedidos,
                onNavigateMenu = onNavigateMenu,
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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Tarjetas superiores de resumen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TarjetaContador(
                    numero = nuevos,
                    etiqueta = "Nuevos",
                    colorFondo = if (esModoOscuro) Color(0xFF0C4A6E) else Color(0xFFE0F2FE),
                    colorTexto = if (esModoOscuro) Color(0xFF38BDF8) else Color(0xFF0284C7),
                    modifier = Modifier.weight(1f)
                )
                TarjetaContador(
                    numero = preparando,
                    etiqueta = "Preparando",
                    colorFondo = if (esModoOscuro) Color(0xFF78350F) else Color(0xFFFEF3C7),
                    colorTexto = if (esModoOscuro) Color(0xFFFBBF24) else Color(0xFFD97706),
                    modifier = Modifier.weight(1f)
                )
                TarjetaContador(
                    numero = listos,
                    etiqueta = "Listos",
                    colorFondo = if (esModoOscuro) Color(0xFF14532D) else Color(0xFFDCFCE7),
                    colorTexto = if (esModoOscuro) Color(0xFF4ADE80) else Color(0xFF15803D),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta Ventas del Día
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorPurpuraAdmin)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Ventas del día", fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "S/ %.2f".format(ventasHoy),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$totalPedidosHoy pedidos",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Gráfico de Pedidos por Hora (Rango Fijo: 8h, 9h, 10h, 11h, 12h)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Pedidos por hora",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mapeo seguro de la hora a la clave "8h", "9h", etc.
                    val mapaPedidos = remember(pedidosPorHora) {
                        val mapa = mutableMapOf<String, Int>()
                        pedidosPorHora.forEach { item ->
                            val horaNum = item.hora
                                .replace("h", "")
                                .split(":")
                                .firstOrNull()
                                ?.trim()
                                ?.toIntOrNull()

                            if (horaNum != null) {
                                val clave = "${horaNum}h"
                                mapa[clave] = (mapa[clave] ?: 0) + item.cantidad
                            }
                        }
                        mapa
                    }

                    val horasFijas = listOf("8h", "9h", "10h", "11h", "12h")
                    val maxCantidad = horasFijas.maxOf { mapaPedidos[it] ?: 0 }.coerceAtLeast(1)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        horasFijas.forEach { horaLabel ->
                            val cantidad = mapaPedidos[horaLabel] ?: 0
                            val esMaximo = (cantidad == maxCantidad && cantidad > 0)

                            GraficoBarraHora(
                                horaLabel = horaLabel,
                                cantidad = cantidad,
                                maxCantidad = maxCantidad,
                                esMaximo = esMaximo,
                                colorPurpura = colorPurpuraAdmin
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de Nuevos Pedidos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nuevos pedidos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = onNavigatePedidos) {
                    Text("Ver todos", color = colorPurpuraAdmin, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (nuevosPedidos.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay nuevos pedidos pendientes",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    nuevosPedidos.take(3).forEach { pedConDetalle ->
                        TarjetaNuevoPedidoPanel(
                            pedConDetalle = pedConDetalle,
                            productosMenu = productosMenu,
                            colorPurpuraAdmin = colorPurpuraAdmin,
                            onPreparar = { panelViewModel.prepararPedido(pedConDetalle.pedido.id) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TarjetaContador(
    numero: Int,
    etiqueta: String,
    colorFondo: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$numero",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = etiqueta,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = colorTexto
            )
        }
    }
}

@Composable
private fun GraficoBarraHora(
    horaLabel: String,
    cantidad: Int,
    maxCantidad: Int,
    esMaximo: Boolean,
    colorPurpura: Color
) {
    val colorBarraNormal = Color(0xFFC7D2FE) // Morado pastel claro
    val colorBarraMax = colorPurpura         // Morado destacado

    val proporcion = if (maxCantidad > 0 && cantidad > 0) (cantidad.toFloat() / maxCantidad.toFloat()).coerceIn(0.25f, 1f) else 0f
    val alturaBarra = if (cantidad > 0) (55 * proporcion).dp else 6.dp
    val colorActual = if (esMaximo) colorBarraMax else colorBarraNormal

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(48.dp)
    ) {
        // 1. Número arriba de la barra (Solo si la cantidad es mayor a 0)
        Box(
            modifier = Modifier.height(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (cantidad > 0) {
                Text(
                    text = "$cantidad",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 2. Contenedor de la barra con altura fija para evitar que empuje los textos
        Box(
            modifier = Modifier
                .width(38.dp)
                .height(60.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(alturaBarra)
                    .background(
                        color = if (cantidad > 0) colorActual else colorBarraNormal.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Hora perfectamente alineada abajo (8h, 9h, 10h, 11h, 12h)
        Text(
            text = horaLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TarjetaNuevoPedidoPanel(
    pedConDetalle: PedidoConDetalle,
    productosMenu: List<Producto>,
    colorPurpuraAdmin: Color,
    onPreparar: () -> Unit
) {
    val ped = pedConDetalle.pedido
    val nombreCliente = if (ped.clienteId == 1) "Alexis Prieto" else "Cliente #${ped.clienteId}"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "#%04d %s".format(ped.id, nombreCliente),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                val resumen = pedConDetalle.detalles.joinToString(", ") { d ->
                    val nombre = productosMenu.find { it.id == d.productoId }?.nombre ?: "Producto"
                    "${d.cantidad} x $nombre"
                }
                Text(
                    text = "$resumen · ${ped.horaRecojo}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = onPreparar,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorPurpuraAdmin)
            ) {
                Text("Preparar", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun BarraNavegacionAdmin(
    pestanaActual: String,
    onNavigatePanel: () -> Unit,
    onNavigatePedidos: () -> Unit,
    onNavigateMenu: () -> Unit,
    onNavigateAjustes: () -> Unit
) {
    val colorPurpuraAdmin = Color(0xFF4F46E5)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationBarItem(
            selected = pestanaActual == "Panel",
            onClick = onNavigatePanel,
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Panel") },
            label = { Text("Panel") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorPurpuraAdmin,
                selectedTextColor = colorPurpuraAdmin,
                indicatorColor = colorPurpuraAdmin.copy(alpha = 0.15f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = pestanaActual == "Pedidos",
            onClick = onNavigatePedidos,
            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Pedidos") },
            label = { Text("Pedidos") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorPurpuraAdmin,
                selectedTextColor = colorPurpuraAdmin,
                indicatorColor = colorPurpuraAdmin.copy(alpha = 0.15f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = pestanaActual == "Menú",
            onClick = onNavigateMenu,
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "Menú") },
            label = { Text("Menú") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorPurpuraAdmin,
                selectedTextColor = colorPurpuraAdmin,
                indicatorColor = colorPurpuraAdmin.copy(alpha = 0.15f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = pestanaActual == "Ajustes",
            onClick = onNavigateAjustes,
            icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
            label = { Text("Ajustes") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorPurpuraAdmin,
                selectedTextColor = colorPurpuraAdmin,
                indicatorColor = colorPurpuraAdmin.copy(alpha = 0.15f),
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}