package com.prieto.cafecampus.ui.admin

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.ui.components.AppTopBar
import com.prieto.cafecampus.ui.components.EstadoChip
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.PedidosAdminViewModel

@Composable
fun PedidosAdminScreen(
    pedidosAdminViewModel: PedidosAdminViewModel,
    menuViewModel: MenuViewModel,
    onVolver: () -> Unit,
    onPedidoClick: (Int) -> Unit,
    onNavigatePanel: () -> Unit,
    onNavigateMenu: () -> Unit,
    onNavigateAjustes: () -> Unit
) {
    val estadoSeleccionado by pedidosAdminViewModel.estadoSeleccionado.collectAsState()
    val pedidosList by pedidosAdminViewModel.pedidos.collectAsState()
    val nombresClientes by pedidosAdminViewModel.nombresClientes.collectAsState()
    val productosMenu by menuViewModel.productos.collectAsState()

    val colorPurpuraAdmin = Color(0xFF4F46E5)

    val filtros = listOf(
        Pair("RECIBIDO", "Nuevos"),
        Pair("PREPARANDO", "Preparando"),
        Pair("LISTO", "Listos"),
        Pair("HISTORIAL", "Historial")
    )

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = "Pedidos",
            )
        },
        bottomBar = {
            BarraNavegacionAdmin(
                pestanaActual = "Pedidos",
                onNavigatePanel = onNavigatePanel,
                onNavigatePedidos = { },
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
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filtros) { (claveEstado, etiqueta) ->
                    val seleccionado = estadoSeleccionado.uppercase() == claveEstado.uppercase()
                    FilterChip(
                        selected = seleccionado,
                        onClick = { pedidosAdminViewModel.cambiarFiltro(claveEstado) },
                        label = { Text(etiqueta, fontSize = 13.sp) },
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

            if (pedidosList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay pedidos en este estado",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(pedidosList) { pedConDetalle ->
                        val nombreReal = nombresClientes[pedConDetalle.pedido.clienteId]
                            ?: "Cliente #${pedConDetalle.pedido.clienteId}"

                        TarjetaPedidoAdmin(
                            pedConDetalle = pedConDetalle,
                            nombreCliente = nombreReal,
                            productosMenu = productosMenu,
                            colorPurpuraAdmin = colorPurpuraAdmin,
                            siguienteTexto = pedidosAdminViewModel.obtenerTextoBotonAvanzar(pedConDetalle.pedido.estado.toString()),
                            onClickCard = { onPedidoClick(pedConDetalle.pedido.id) },
                            onAvanzar = { pedidosAdminViewModel.avanzar(pedConDetalle) },
                            onCancelar = { pedidosAdminViewModel.cancelar(pedConDetalle) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaPedidoAdmin(
    pedConDetalle: PedidoConDetalle,
    nombreCliente: String,
    productosMenu: List<Producto>,
    colorPurpuraAdmin: Color,
    siguienteTexto: String,
    onClickCard: () -> Unit,
    onAvanzar: () -> Unit,
    onCancelar: () -> Unit
) {
    val ped = pedConDetalle.pedido
    val detalles = pedConDetalle.detalles

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickCard() },
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = colorPurpuraAdmin,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "#%04d %s".format(ped.id, nombreCliente),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                EstadoChip(estado = ped.estado.toString())
            }

            Spacer(modifier = Modifier.height(10.dp))

            val resumenProductos = detalles.joinToString("\n") { d ->
                val nombre = productosMenu.find { it.id == d.productoId }?.nombre ?: "Producto"
                "${d.cantidad} x $nombre"
            }
            Text(
                text = resumenProductos,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Recojo ${ped.horaRecojo}  ·  S/ %.2f".format(ped.total) +
                        if (ped.nota.isNotEmpty()) "  ·  Nota: ${ped.nota}" else "",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            val estadoUpper = ped.estado.toString().uppercase()
            if (estadoUpper != "ENTREGADO" && estadoUpper != "CANCELADO") {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onAvanzar,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorPurpuraAdmin)
                    ) {
                        Text(siguienteTexto, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    OutlinedButton(
                        onClick = onCancelar,
                        modifier = Modifier
                            .weight(0.7f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFEE2E2),
                            contentColor = Color(0xFFDC2626)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                    ) {
                        Text("Cancelar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}