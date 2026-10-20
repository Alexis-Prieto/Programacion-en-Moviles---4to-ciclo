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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val nuevos by panelViewModel.nuevos.collectAsState()
    val preparando by panelViewModel.preparando.collectAsState()
    val listos by panelViewModel.listos.collectAsState()
    val ventasHoy by panelViewModel.ventasHoy.collectAsState()
    val totalPedidosHoy by panelViewModel.totalPedidosHoy.collectAsState()
    val nuevosPedidos by panelViewModel.nuevosPedidosLista.collectAsState()
    val productosMenu by menuViewModel.productos.collectAsState()

    val colorPurpuraAdmin = Color(0xFF4F46E5)
    val colorFondoPantalla = Color(0xFFF8FAFC)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Panel de control",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "Cafetería · Hoy",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFF1F5F9), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = Color(0xFF1F2937),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorFondoPantalla)
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
        containerColor = colorFondoPantalla
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
                    colorFondo = Color(0xFFE0F2FE),
                    colorTexto = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f)
                )
                TarjetaContador(
                    numero = preparando,
                    etiqueta = "Preparando",
                    colorFondo = Color(0xFFFEF3C7),
                    colorTexto = Color(0xFFD97706),
                    modifier = Modifier.weight(1f)
                )
                TarjetaContador(
                    numero = listos,
                    etiqueta = "Listos",
                    colorFondo = Color(0xFFDCFCE7),
                    colorTexto = Color(0xFF15803D),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Pedidos por hora",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        GraficoBarraHora(hora = "8h", cantidad = 3, max = 9, color = Color(0xFFC7D2FE))
                        GraficoBarraHora(hora = "9h", cantidad = 6, max = 9, color = Color(0xFFC7D2FE))
                        GraficoBarraHora(hora = "10h", cantidad = 9, max = 9, color = colorPurpuraAdmin)
                        GraficoBarraHora(hora = "11h", cantidad = 5, max = 9, color = Color(0xFFC7D2FE))
                        GraficoBarraHora(hora = "12h", cantidad = 7, max = 9, color = Color(0xFFC7D2FE))
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nuevos pedidos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
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
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                ) {
                    Box(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay nuevos pedidos pendientes", color = Color.Gray, fontSize = 13.sp)
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
private fun GraficoBarraHora(hora: String, cantidad: Int, max: Int, color: Color) {
    val porcentaje = (cantidad.toFloat() / max.toFloat()).coerceIn(0.1f, 1f)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$cantidad", fontSize = 10.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .width(28.dp)
                .height((70 * porcentaje).dp)
                .background(color, shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(hora, fontSize = 11.sp, color = Color.Gray)
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
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
                    color = Color(0xFF1F2937)
                )
                Spacer(modifier = Modifier.height(2.dp))
                val resumen = pedConDetalle.detalles.joinToString(", ") { d ->
                    val nombre = productosMenu.find { it.id == d.productoId }?.nombre ?: "Producto"
                    "${d.cantidad} x $nombre"
                }
                Text(
                    text = "$resumen · ${ped.horaRecojo}",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
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
    val colorPurpuraContenedor = Color(0xFFEEF2FF)

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = pestanaActual == "Panel",
            onClick = onNavigatePanel,
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Panel") },
            label = { Text("Panel") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorPurpuraAdmin,
                selectedTextColor = colorPurpuraAdmin,
                indicatorColor = colorPurpuraContenedor,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
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
                indicatorColor = colorPurpuraContenedor,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
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
                indicatorColor = colorPurpuraContenedor,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
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
                indicatorColor = colorPurpuraContenedor,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
    }
}