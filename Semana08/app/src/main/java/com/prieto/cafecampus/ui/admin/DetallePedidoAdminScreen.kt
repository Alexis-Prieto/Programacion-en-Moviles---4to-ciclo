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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.PedidosAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoAdminScreen(
    pedidoId: Int,
    pedidosAdminViewModel: PedidosAdminViewModel,
    menuViewModel: MenuViewModel,
    onVolver: () -> Unit
) {
    val todosPedidos by pedidosAdminViewModel.pedidos.collectAsState()
    val productosMenu by menuViewModel.productos.collectAsState()
    val pedConDetalle = todosPedidos.find { it.pedido.id == pedidoId }

    val colorPurpuraAdmin = Color(0xFF4F46E5)
    val colorFondoPantalla = Color(0xFFF8FAFC)

    val estadosTransicion = listOf("RECIBIDO", "PREPARANDO", "LISTO", "ENTREGADO")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detalle del pedido",
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
        containerColor = colorFondoPantalla
    ) { paddingValues ->
        if (pedConDetalle == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Pedido no encontrado", color = Color.Gray)
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
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
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
                            color = Color(0xFF1F2937)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Recojo ${ped.horaRecojo}",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }

                    EstadoBadgeChipAdmin(estado = estadoActual)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val nombreCliente = if (ped.clienteId == 1) "Alexis Prieto · Estudiante" else "Cliente #${ped.clienteId}"

                    Text("Cliente", fontSize = 12.sp, color = Color(0xFF6B7280))
                    Text(
                        text = nombreCliente,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
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
                                color = Color(0xFF374151),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "x${item.cantidad}",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Text(
                                text = "S/ %.2f".format(item.precioUnitario * item.cantidad),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF9C3)),
                    border = BorderStroke(1.dp, Color(0xFFFEF08A))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nota del cliente", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF854D0E))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(ped.nota, fontSize = 13.sp, color = Color(0xFFA16207))
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text("Cambiar estado", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
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
                                color = if (sel) colorPurpuraAdmin else Color.White,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(1.dp, if (sel) colorPurpuraAdmin else Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                            .clickable { pedidosAdminViewModel.cambiarEstadoPorId(ped.id, est) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = est.lowercase().replaceFirstChar { it.uppercase() },
                            fontSize = 11.sp,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            color = if (sel) Color.White else Color(0xFF64748B)
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
                    Text(siguienteTexto, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                    border = BorderStroke(1.5.dp, Color(0xFFDC2626))
                ) {
                    Text("Cancelar pedido", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}