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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.PanelAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelScreen(
    panelViewModel: PanelAdminViewModel,
    menuViewModel: MenuViewModel,
    onNavigatePedidos: () -> Unit,
    onNavigateMenu: () -> Unit,
    onNavigateAjustes: () -> Unit
) {
    val esModoOscuro by panelViewModel.modoOscuro.collectAsState()

    val nuevos by panelViewModel.nuevos.collectAsState()
    val preparando by panelViewModel.preparando.collectAsState()
    val listos by panelViewModel.listos.collectAsState()
    val ventasHoy by panelViewModel.ventasHoy.collectAsState()
    val totalPedidosHoy by panelViewModel.totalPedidosHoy.collectAsState()
    val nuevosPedidos by panelViewModel.nuevosPedidosLista.collectAsState()
    val nombresClientes by panelViewModel.nombresClientes.collectAsState()
    val productosMenu by menuViewModel.productos.collectAsState()

    val pedidosPorHora by panelViewModel.pedidosPorHora.collectAsState()
    val productoMasVendido by panelViewModel.productoMasVendido.collectAsState()
    val totalesPorFormaPago by panelViewModel.totalesPorFormaPago.collectAsState()

    val colorPurpuraAdmin = Color(0xFF4F46E5)
    val colorVerdeVentas = Color(0xFF16A34A)

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

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorPurpuraAdmin)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Ventas acumuladas hoy", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "S/ %.2f".format(ventasHoy),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$totalPedidosHoy pedidos",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Reporte del día",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Producto más vendido",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        if (productoMasVendido != null) {
                            Text(
                                text = productoMasVendido!!.nombreProducto,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${productoMasVendido!!.cantidadTotal} unidades vendidas hoy",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Text(
                                text = "Sin ventas registradas hoy",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Ventas por forma de pago",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (totalesPorFormaPago.isEmpty()) {
                        Text(
                            text = "No hay registros de pagos hoy",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            totalesPorFormaPago.forEach { item ->
                                val iconoPago = when (item.formaPago.lowercase()) {
                                    "efectivo" -> Icons.Default.LocalAtm
                                    "tarjeta" -> Icons.Default.CreditCard
                                    else -> Icons.Default.Payments
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = iconoPago,
                                            contentDescription = null,
                                            tint = colorPurpuraAdmin,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = item.formaPago.ifBlank { "Efectivo" },
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Text(
                                        text = "S/ %.2f".format(item.totalAcumulado),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorVerdeVentas
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Pedidos por hora",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

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
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nuevos pedidos",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = onNavigatePedidos) {
                    Text("Ver todos", color = colorPurpuraAdmin, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (nuevosPedidos.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .padding(18.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay pedidos pendientes por atender",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    nuevosPedidos.take(3).forEach { pedConDetalle ->
                        val nombreReal = nombresClientes[pedConDetalle.pedido.clienteId]
                            ?: "Cliente #${pedConDetalle.pedido.clienteId}"

                        TarjetaNuevoPedidoPanel(
                            pedConDetalle = pedConDetalle,
                            nombreCliente = nombreReal,
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
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$numero",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = etiqueta,
                fontSize = 11.sp,
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
    val colorBarraNormal = Color(0xFFC7D2FE)
    val colorBarraMax = colorPurpura

    val proporcion = if (maxCantidad > 0 && cantidad > 0) (cantidad.toFloat() / maxCantidad.toFloat()).coerceIn(0.25f, 1f) else 0f
    val alturaBarra = if (cantidad > 0) (50 * proporcion).dp else 6.dp
    val colorActual = if (esMaximo) colorBarraMax else colorBarraNormal

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(44.dp)
    ) {
        Box(
            modifier = Modifier.height(18.dp),
            contentAlignment = Alignment.Center
        ) {
            if (cantidad > 0) {
                Text(
                    text = "$cantidad",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .width(34.dp)
                .height(52.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(alturaBarra)
                    .background(
                        color = if (cantidad > 0) colorActual else colorBarraNormal.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = horaLabel,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TarjetaNuevoPedidoPanel(
    pedConDetalle: PedidoConDetalle,
    nombreCliente: String,
    productosMenu: List<Producto>,
    colorPurpuraAdmin: Color,
    onPreparar: () -> Unit
) {
    val ped = pedConDetalle.pedido

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "#%04d · %s".format(ped.id, nombreCliente),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                val resumen = pedConDetalle.detalles.joinToString(", ") { d ->
                    val nombre = productosMenu.find { it.id == d.productoId }?.nombre ?: d.nombreProducto
                    "${d.cantidad} x $nombre"
                }
                Text(
                    text = "$resumen · ${ped.horaRecojo}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onPreparar,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorPurpuraAdmin)
            ) {
                Text("Preparar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
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