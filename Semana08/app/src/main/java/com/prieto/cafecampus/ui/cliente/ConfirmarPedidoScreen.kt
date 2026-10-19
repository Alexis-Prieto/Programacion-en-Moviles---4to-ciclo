package com.prieto.cafecampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import com.prieto.cafecampus.model.ItemCarrito
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarPedidoScreen(
    menuViewModel: MenuViewModel,
    pedidosViewModel: PedidosClienteViewModel,
    onVolver: () -> Unit,
    onPedidoConfirmado: (Int) -> Unit
) {
    val carrito by menuViewModel.carrito.collectAsState()
    val totalCalculado = menuViewModel.total()

    var horaRecojo by remember { mutableStateOf("10:30") }
    var formaPago by remember { mutableStateOf("Pagar en caja") }

    val horasDisponibles = listOf("Ahora", "10:30", "11:00", "11:30")
    val formasPagoDisponibles = listOf("Pagar en caja", "Yape / Plin")

    val colorNaranjaPrimario = Color(0xFFEA580C)
    val colorNaranjaContenedor = Color(0xFFFFEDD5)
    val colorFondoPantalla = Color(0xFFFFFBF7)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Confirmar pedido",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF3F4F6))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    carrito.forEachIndexed { index, item ->
                        ItemResumenRow(item = item, colorContenedor = colorNaranjaContenedor)
                        if (index < carrito.size - 1) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFFF3F4F6))
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Hora de recojo",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                horasDisponibles.forEach { hora ->
                    FilterChip(
                        selected = horaRecojo == hora,
                        onClick = { horaRecojo = hora },
                        label = { Text(hora, fontSize = 13.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colorNaranjaPrimario,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF4B5563)
                        ),
                        border = null,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Forma de pago",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                formasPagoDisponibles.forEach { pago ->
                    val seleccionado = formaPago == pago
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                color = if (seleccionado) colorNaranjaContenedor else Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (seleccionado) 1.5.dp else 1.dp,
                                color = if (seleccionado) colorNaranjaPrimario else Color(0xFFE5E7EB),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { formaPago = pago },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pago,
                            fontSize = 13.sp,
                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
                            color = if (seleccionado) colorNaranjaPrimario else Color(0xFF4B5563)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF3F4F6))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", fontSize = 14.sp, color = Color(0xFF6B7280))
                        Text("S/ %.2f".format(totalCalculado), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Servicio", fontSize = 14.sp, color = Color(0xFF6B7280))
                        Text("S/ 0.00", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                        Text("S/ %.2f".format(totalCalculado), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colorNaranjaPrimario)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val notaResumen = carrito.mapNotNull { it.nota.ifEmpty { null } }.joinToString("; ")
                    pedidosViewModel.confirmar(
                        items = carrito,
                        horaRecojo = horaRecojo,
                        pago = formaPago,
                        nota = notaResumen
                    ) { nuevoPedidoId ->
                        menuViewModel.vaciar()
                        onPedidoConfirmado(nuevoPedidoId)
                    }
                },
                enabled = carrito.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorNaranjaPrimario,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text("Confirmar pedido", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colorNaranjaPrimario),
                border = BorderStroke(1.5.dp, colorNaranjaPrimario)
            ) {
                Text("Seguir pidiendo", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
@Composable
private fun ItemResumenRow(item: ItemCarrito, colorContenedor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(colorContenedor, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            RenderizarIconoMini(iconoNombre = item.producto.icono, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.producto.nombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "x${item.cantidad}",
                fontSize = 13.sp,
                color = Color(0xFF9CA3AF)
            )
        }
        Text(
            text = "S/ %.2f".format(item.producto.precio * item.cantidad),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )
    }
}
@Composable
private fun RenderizarIconoMini(iconoNombre: String, modifier: Modifier = Modifier) {
    when {
        iconoNombre.contains("plato", ignoreCase = true) || iconoNombre.contains("almuerzo", ignoreCase = true) -> IconoPlatoComida(modifier)
        iconoNombre.contains("bebida", ignoreCase = true) || iconoNombre.contains("jugo", ignoreCase = true) -> IconoVasoBebida(modifier)
        else -> IconoTazaCafe(modifier)
    }
}

@Composable
private fun IconoTazaCafe(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRoundRect(Color.White, Offset(w * 0.08f, h * 0.72f), Size(w * 0.84f, h * 0.09f), CornerRadius(h * 0.04f))
        drawRoundRect(Color.White, Offset(w * 0.18f, h * 0.25f), Size(w * 0.52f, h * 0.44f), CornerRadius(w * 0.08f))
        drawRoundRect(Color(0xFF5A3825), Offset(w * 0.22f, h * 0.29f), Size(w * 0.44f, h * 0.14f), CornerRadius(w * 0.05f))
    }
}
@Composable
private fun IconoPlatoComida(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val center = Offset(w / 2f, h / 2f)
        drawCircle(Color.White, radius = w * 0.40f, center = center, style = Stroke(width = w * 0.13f))
        val r = w * 0.08f
        drawCircle(Color(0xFFDC2626), radius = r, center = Offset(center.x, center.y - r * 0.95f))
        drawCircle(Color(0xFFF59E0B), radius = r, center = Offset(center.x - r * 0.9f, center.y + r * 0.65f))
        drawCircle(Color(0xFF16A34A), radius = r, center = Offset(center.x + r * 0.9f, center.y + r * 0.65f))
    }
}
@Composable
private fun IconoVasoBebida(modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strawPath = Path().apply {
            moveTo(w * 0.68f, h * 0.08f)
            lineTo(w * 0.52f, h * 0.42f)
        }
        drawPath(strawPath, Color(0xFF16A34A), style = Stroke(width = w * 0.07f, cap = StrokeCap.Round))
        val glassTopPath = Path().apply {
            moveTo(w * 0.22f, h * 0.30f)
            lineTo(w * 0.78f, h * 0.30f)
            lineTo(w * 0.73f, h * 0.56f)
            lineTo(w * 0.27f, h * 0.56f)
            close()
        }
        drawPath(glassTopPath, Color.White)
        val liquidPath = Path().apply {
            moveTo(w * 0.27f, h * 0.56f)
            lineTo(w * 0.73f, h * 0.56f)
            lineTo(w * 0.68f, h * 0.88f)
            lineTo(w * 0.32f, h * 0.88f)
            close()
        }
        drawPath(liquidPath, Color(0xFFEA580C))
    }
}