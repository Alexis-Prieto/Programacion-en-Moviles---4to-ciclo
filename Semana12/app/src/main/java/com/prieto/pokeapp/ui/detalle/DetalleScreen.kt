package com.prieto.pokeapp.ui.detalle

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.prieto.pokeapp.domain.model.DetalleItem
import com.prieto.pokeapp.ui.components.fuegoFondo
import com.prieto.pokeapp.ui.UiState
import com.prieto.pokeapp.ui.theme.PokedexDarkRed
import com.prieto.pokeapp.ui.theme.PokedexRed
import com.prieto.pokeapp.ui.theme.PokedexScreenBlue
import com.prieto.pokeapp.ui.theme.PokedexYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val esFavorito by viewModel.esFavorito.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.background(Brush.horizontalGradient(listOf(Color(0xFFAA1720), Color(0xFFE93B22), Color(0xFFFF8D28)))),
                title = {
                    Text(
                        text = "Detalle Pokémon",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent,
        modifier = modifier.fuegoFondo()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is UiState.Cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = PokedexRed,
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Cargando detalle...",
                                color = PokedexDarkRed,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                is UiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ElevatedCard(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error al Cargar Detalle",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = state.mensaje,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.cargarDetalle() },
                                    colors = ButtonDefaults.buttonColors(containerColor = PokedexRed)
                                ) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }
                is UiState.Exito -> {
                    DetalleContenido(
                        detalle = state.datos,
                        esFavorito = esFavorito,
                        onFavoritoClick = { viewModel.alternarFavorito() }
                    )
                }
            }
        }
    }
}

@Composable
fun DetalleContenido(
    detalle: DetalleItem,
    esFavorito: Boolean,
    onFavoritoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var movimientosExpandidos by rememberSaveable(detalle.id) { mutableStateOf(false) }
    val heartScale by animateFloatAsState(
        targetValue = if (esFavorito) 1.25f else 1.0f,
        label = "HeartScale"
    )
    val heartTint by animateColorAsState(
        targetValue = PokedexRed,
        label = "HeartTint"
    )

    val imagenUrl = detalle.urlImagen
        ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${detalle.id}.png"

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(28.dp))
                .height(280.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFFC82520), Color(0xFFF16A2C), Color(0xFFFFC46B)))),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(230.dp).clip(CircleShape)
                    .background(Color(0xFFFFE0A6).copy(alpha = 0.35f))
                    .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imagenUrl,
                    contentDescription = detalle.nombre,
                    modifier = Modifier.size(215.dp)
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFFFFAF3))
                .border(1.dp, Color(0xFFF1D8C2), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "#${detalle.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF786A62)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = detalle.nombre.replaceFirstChar { it.uppercase() },
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF32181A)
                )
                IconButton(
                    onClick = onFavoritoClick,
                    modifier = Modifier.clip(CircleShape).background(Color(0xFFFFECE5))
                ) {
                    Icon(
                        imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = heartTint,
                        modifier = Modifier.size(32.dp).scale(heartScale)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Medidas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF651C22)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ElevatedCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = androidx.compose.material3.CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFF9F1))
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Height, contentDescription = null, tint = Color(0xFFD85C24), modifier = Modifier.size(26.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("ALTURA", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF786A62))
                        Text("${detalle.altura / 10.0} m", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF32181A))
                    }
                }
                ElevatedCard(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = androidx.compose.material3.CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFF9F1))
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Color(0xFFD85C24), modifier = Modifier.size(26.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("PESO", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF786A62))
                        Text("${detalle.peso / 10.0} kg", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF32181A))
                    }
                }
            }
            if (detalle.tipos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Tipos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PokedexDarkRed)
                Text(
                    text = detalle.tipos.joinToString(" · ") { it.replace('-', ' ').replaceFirstChar { letra -> letra.uppercase() } },
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            detalle.experienciaBase?.let { experiencia ->
                Spacer(modifier = Modifier.height(20.dp))
                Text("Experiencia base", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PokedexDarkRed)
                Text(text = experiencia.toString(), modifier = Modifier.padding(top = 6.dp), style = MaterialTheme.typography.bodyLarge)
            }
            if (detalle.habilidades.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Habilidades", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PokedexDarkRed)
                detalle.habilidades.forEach { habilidad ->
                    Text(
                        text = habilidad.nombre.replace('-', ' ').replaceFirstChar { it.uppercase() } + if (habilidad.oculta) " (oculta)" else "",
                        modifier = Modifier.padding(top = 6.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            if (detalle.estadisticas.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Estadísticas base", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PokedexDarkRed)
                detalle.estadisticas.forEach { estadistica ->
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(estadistica.nombre.replace('-', ' ').replaceFirstChar { it.uppercase() }, modifier = Modifier.weight(1f))
                        Text(estadistica.valor.toString(), fontWeight = FontWeight.Bold)
                    }
                }
            }
            if (detalle.movimientos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Movimientos (${detalle.movimientos.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PokedexDarkRed)
                TextButton(onClick = { movimientosExpandidos = !movimientosExpandidos }) {
                    Text(if (movimientosExpandidos) "Ocultar movimientos" else "Ver movimientos")
                }
                if (movimientosExpandidos) {
                    detalle.movimientos.forEach { movimiento ->
                        Text(
                            text = movimiento.replace('-', ' ').replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(vertical = 4.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
