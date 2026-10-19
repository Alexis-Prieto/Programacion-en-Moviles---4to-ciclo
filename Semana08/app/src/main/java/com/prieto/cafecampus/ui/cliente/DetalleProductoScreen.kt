package com.prieto.cafecampus.ui.cliente

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    menuViewModel: MenuViewModel,
    onVolver: () -> Unit,
    onProductoAgregado: () -> Unit
) {
    val producto = menuViewModel.obtenerProductoPorId(productoId)
    val colorNaranjaPrimario = Color(0xFFEA580C)
    val colorNaranjaContenedor = Color(0xFFFFEDD5)
    val colorFondoPantalla = Color(0xFFFFFBF7)
    val colorFondoTotal = Color(0xFFFFF2E5)

    var cantidad by remember { mutableIntStateOf(1) }
    var nota by remember { mutableStateOf("") }

    if (producto == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Producto no encontrado", color = Color.Gray)
        }
        return
    }
    val totalCalculado = producto.precio * cantidad

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Detalle del producto",
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
            Surface(
                color = colorFondoPantalla
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 12.dp)
                ) {
                    Button(
                        onClick = {
                            menuViewModel.agregar(producto, cantidad, nota)
                            onProductoAgregado()
                        },
                        enabled = producto.disponible,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorNaranjaPrimario,
                            disabledContainerColor = Color.LightGray
                        )
                    ) {
                        Text(
                            text = if (producto.disponible) "Agregar al pedido" else "Producto Agotado",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        containerColor = colorFondoPantalla
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(colorNaranjaContenedor, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                RenderizarIconoProducto(
                    iconoNombre = producto.icono,
                    modifier = Modifier.size(110.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = producto.nombre,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "S/ %.2f".format(producto.precio),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorNaranjaPrimario
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (producto.disponible) {
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Disponible", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFFDCFCE7),
                            labelColor = Color(0xFF15803D)
                        ),
                        border = null
                    )
                } else {
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Agotado", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFFFEE2E2),
                            labelColor = Color(0xFFB91C1C)
                        ),
                        border = null
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "10 – 15 min",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = producto.descripcion,
                fontSize = 13.sp,
                color = Color(0xFF6B7280),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cantidad",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Row(
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(24.dp))
                        .border(1.dp, Color(0xFFE5E7EB), shape = RoundedCornerShape(24.dp))
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (cantidad > 1) cantidad-- },
                        enabled = producto.disponible && cantidad > 1,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                if (cantidad > 1) colorNaranjaContenedor else Color(0xFFF3F4F6),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Restar",
                            tint = if (cantidad > 1) colorNaranjaPrimario else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = cantidad.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    IconButton(
                        onClick = { cantidad++ },
                        enabled = producto.disponible,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                if (producto.disponible) colorNaranjaPrimario else Color.LightGray,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Sumar",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Nota para la cocina",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nota,
                onValueChange = { nota = it },
                placeholder = { Text("", color = Color(0xFF9CA3AF)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorNaranjaPrimario,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorFondoTotal, shape = RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = "Total",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "S/ %.2f".format(totalCalculado),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorNaranjaPrimario
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Composable
private fun RenderizarIconoProducto(
    iconoNombre: String,
    modifier: Modifier = Modifier
) {
    when {
        iconoNombre.contains("plato", ignoreCase = true) || iconoNombre.contains("almuerzo", ignoreCase = true) -> {
            IconoPlatoComida(modifier = modifier)
        }
        iconoNombre.contains("bebida", ignoreCase = true) || iconoNombre.contains("jugo", ignoreCase = true) -> {
            IconoVasoBebida(modifier = modifier)
        }
        iconoNombre.contains("cafe", ignoreCase = true) -> {
            IconoTazaCafe(modifier = modifier)
        }
        else -> {
            IconoPlatoComida(modifier = modifier)
        }
    }
}
@Composable
private fun IconoTazaCafe(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.08f, h * 0.72f),
            size = Size(w * 0.84f, h * 0.09f),
            cornerRadius = CornerRadius(h * 0.04f, h * 0.04f)
        )
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.18f, h * 0.25f),
            size = Size(w * 0.52f, h * 0.44f),
            cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
        )
        drawRoundRect(
            color = Color(0xFF5A3825),
            topLeft = Offset(w * 0.22f, h * 0.29f),
            size = Size(w * 0.44f, h * 0.14f),
            cornerRadius = CornerRadius(w * 0.05f, w * 0.05f)
        )
        drawArc(
            color = Color.White,
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.62f, h * 0.33f),
            size = Size(w * 0.22f, h * 0.28f),
            style = Stroke(width = w * 0.08f)
        )
    }
}
@Composable
private fun IconoPlatoComida(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val center = Offset(w / 2f, h / 2f)

        drawCircle(
            color = Color.White,
            radius = w * 0.40f,
            center = center,
            style = Stroke(width = w * 0.13f)
        )

        val r = w * 0.08f
        drawCircle(
            color = Color(0xFFDC2626),
            radius = r,
            center = Offset(center.x, center.y - r * 0.95f)
        )
        drawCircle(
            color = Color(0xFFF59E0B),
            radius = r,
            center = Offset(center.x - r * 0.9f, center.y + r * 0.65f)
        )
        drawCircle(
            color = Color(0xFF16A34A),
            radius = r,
            center = Offset(center.x + r * 0.9f, center.y + r * 0.65f)
        )
    }
}
@Composable
private fun IconoVasoBebida(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val strawPath = Path().apply {
            moveTo(w * 0.68f, h * 0.08f)
            lineTo(w * 0.52f, h * 0.42f)
        }
        drawPath(
            path = strawPath,
            color = Color(0xFF16A34A),
            style = Stroke(width = w * 0.07f, cap = StrokeCap.Round)
        )

        val glassTopPath = Path().apply {
            moveTo(w * 0.22f, h * 0.30f)
            lineTo(w * 0.78f, h * 0.30f)
            lineTo(w * 0.73f, h * 0.56f)
            lineTo(w * 0.27f, h * 0.56f)
            close()
        }
        drawPath(path = glassTopPath, color = Color.White)

        val liquidPath = Path().apply {
            moveTo(w * 0.27f, h * 0.56f)
            lineTo(w * 0.73f, h * 0.56f)
            lineTo(w * 0.68f, h * 0.88f)
            lineTo(w * 0.32f, h * 0.88f)
            close()
        }
        drawPath(path = liquidPath, color = Color(0xFFEA580C))

        val outlinePath = Path().apply {
            moveTo(w * 0.22f, h * 0.30f)
            lineTo(w * 0.78f, h * 0.30f)
            lineTo(w * 0.68f, h * 0.88f)
            lineTo(w * 0.32f, h * 0.88f)
            close()
        }
        drawPath(
            path = outlinePath,
            color = Color.White,
            style = Stroke(width = w * 0.04f)
        )
    }
}