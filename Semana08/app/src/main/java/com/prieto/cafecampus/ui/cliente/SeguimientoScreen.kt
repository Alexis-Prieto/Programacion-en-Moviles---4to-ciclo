package com.prieto.cafecampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
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
import com.prieto.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    pedidoId: Int,
    pedidosViewModel: PedidosClienteViewModel,
    menuViewModel: MenuViewModel,
    onVolver: () -> Unit,
    onNavigateMenu: () -> Unit,
    onNavigatePedidos: () -> Unit,
    onNavigatePerfil: () -> Unit
) {
    val pedidoConDetalle by pedidosViewModel.pedido(pedidoId).collectAsState()

    val colorNaranjaPrimario = Color(0xFFEA580C)
    val colorNaranjaContenedor = Color(0xFFFFEDD5)
    val colorFondoPantalla = Color(0xFFFFFBF7)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Seguimiento",
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
                    onClick = onNavigatePedidos,
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
        if (pedidoConDetalle == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando pedido...", color = Color.Gray)
            }
            return@Scaffold
        }

        val ped = pedidoConDetalle!!.pedido
        val detalles = pedidoConDetalle!!.detalles

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
                colors = CardDefaults.cardColors(containerColor = colorNaranjaPrimario)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Pedido #%04d".format(ped.id),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Recojo a las ${ped.horaRecojo}",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tiempo estimado: 10 min",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(28.dp))

            StepperEstado(
                estadoActual = ped.estado.toString(),
                colorNaranjaPrimario = colorNaranjaPrimario
            )

            Spacer(modifier = Modifier.height(28.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF3F4F6))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val resumenTexto = detalles.joinToString(" · ") { item ->
                        val nombreProd = menuViewModel.obtenerProductoPorId(item.productoId)?.nombre ?: "Producto"
                        "${item.cantidad} x $nombreProd"
                    }
                    Text(
                        text = resumenTexto,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Total  S/ %.2f".format(ped.total),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorNaranjaPrimario
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
@Composable
private fun StepperEstado(
    estadoActual: String,
    colorNaranjaPrimario: Color
) {
    val estados = listOf("RECIBIDO", "PREPARANDO", "LISTO", "ENTREGADO")
    val titulos = listOf("Recibido", "Preparando", "Listo", "Entregado")
    val subtitulos = listOf(
        "Tu pedido llegó a la cocina",
        "Están cocinando tu pedido",
        "Puedes pasar a recogerlo",
        "¡Buen provecho!"
    )

    val indiceActual = estados.indexOf(estadoActual.uppercase()).let { if (it == -1) 0 else it }

    Column(modifier = Modifier.fillMaxWidth()) {
        estados.forEachIndexed { index, _ ->
            val esCompletado = index < indiceActual
            val esActual = index == indiceActual

            Row(verticalAlignment = Alignment.Top) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                color = when {
                                    esCompletado -> Color(0xFF16A34A)
                                    esActual -> colorNaranjaPrimario
                                    else -> Color.Transparent
                                },
                                shape = CircleShape
                            )
                            .then(
                                if (!esCompletado && !esActual) {
                                    Modifier.border(2.dp, Color(0xFFD1D5DB), CircleShape)
                                } else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (esCompletado) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        } else if (esActual) {
                            Box(modifier = Modifier.size(10.dp).background(Color.White, shape = CircleShape))
                        }
                    }
                    if (index < estados.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(44.dp)
                                .background(
                                    when {
                                        index < indiceActual -> colorNaranjaPrimario
                                        else -> Color(0xFFE5E7EB)
                                    }
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = titulos[index],
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (esCompletado || esActual) Color(0xFF1F2937) else Color(0xFF9CA3AF)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitulos[index],
                        fontSize = 12.sp,
                        color = if (esCompletado || esActual) Color(0xFF6B7280) else Color(0xFF9CA3AF)
                    )
                }
            }
        }
    }
}