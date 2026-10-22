package com.prieto.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.cafecampus.ui.components.AppTopBar
import com.prieto.cafecampus.ui.components.EstadoChip
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.PedidosAdminViewModel

@Composable
fun DetallePedidoAdminScreen(
    pedidoId: Int,
    pedidosAdminViewModel: PedidosAdminViewModel,
    menuViewModel: MenuViewModel,
    onVolver: () -> Unit
) {
    val todosPedidos by pedidosAdminViewModel.pedidos.collectAsState()
    val productosMenu by menuViewModel.productos.collectAsState()
    val nombresClientes by pedidosAdminViewModel.nombresClientes.collectAsState()

    val pedConDetalle = todosPedidos.find { it.pedido.id == pedidoId }

    val colorPurpuraAdmin = Color(0xFF4F46E5)
    val colorRojoCancelar = MaterialTheme.colorScheme.error
    val estadosTransicion = listOf("RECIBIDO", "PREPARANDO", "LISTO", "ENTREGADO")

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = "Detalle del pedido",
                onBack = onVolver
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (pedConDetalle == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pedido no encontrado",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Scaffold
        }

        val ped = pedConDetalle.pedido
        val detalles = pedConDetalle.detalles
        val estadoActual = ped.estado.toString().uppercase()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Pedido #%04d".format(ped.id),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Recojo ${ped.horaRecojo}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    EstadoChip(estado = estadoActual)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Consulta dinámica del nombre del cliente desde el ViewModel
                    val nombreCliente = nombresClientes[ped.clienteId] ?: "Cliente #${ped.clienteId}"

                    Text(
                        text = "Cliente",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$nombreCliente · Estudiante",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    detalles.forEach { item ->
                        val prodNombre = productosMenu.find { it.id == item.productoId }?.nombre
                            ?: menuViewModel.obtenerProductoPorId(item.productoId)?.nombre
                            ?: "Producto"

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = prodNombre,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "x${item.cantidad}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Text(
                                text = "S/ %.2f".format(item.precioUnitario * item.cantidad),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "S/ %.2f".format(ped.total),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorPurpuraAdmin
                        )
                    }
                }
            }
            if (ped.nota.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Nota del cliente",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ped.nota,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Cambiar estado",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                estadosTransicion.forEach { est ->
                    val sel = estadoActual == est
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .background(
                                color = if (sel) colorPurpuraAdmin else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (sel) colorPurpuraAdmin else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { pedidosAdminViewModel.cambiarEstadoPorId(ped.id, est) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = est.lowercase().replaceFirstChar { it.uppercase() },
                            fontSize = 11.sp,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            color = if (sel) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            val siguienteTexto = pedidosAdminViewModel.obtenerTextoBotonAvanzar(estadoActual)
            if (estadoActual != "ENTREGADO" && estadoActual != "CANCELADO") {
                Button(
                    onClick = {
                        pedidosAdminViewModel.avanzar(pedConDetalle)
                        onVolver()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorPurpuraAdmin)
                ) {
                    Text(
                        text = siguienteTexto,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        pedidosAdminViewModel.cancelarPorId(ped.id)
                        onVolver()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colorRojoCancelar),
                    border = BorderStroke(1.5.dp, colorRojoCancelar)
                ) {
                    Text("Cancelar pedido", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}