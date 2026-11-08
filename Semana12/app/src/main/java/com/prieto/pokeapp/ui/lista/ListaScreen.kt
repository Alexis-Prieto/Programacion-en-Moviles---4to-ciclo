package com.prieto.pokeapp.ui.lista

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.ui.components.fuegoFondo
import com.prieto.pokeapp.ui.UiState
import com.prieto.pokeapp.ui.theme.PokedexRed
import com.prieto.pokeapp.ui.theme.PokedexYellow

@Composable
private fun MiBottomNavBar(
    rutaActual: String,
    onNavigate: (String) -> Unit,
    esAdmin: Boolean
) {
    NavigationBar(
        containerColor = Color(0xFFFFF5EA)
    ) {
        NavigationBarItem(
            selected = rutaActual == "lista",
            onClick = { if (rutaActual != "lista") onNavigate("lista") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = rutaActual == "favoritos",
            onClick = { if (rutaActual != "favoritos") onNavigate("favoritos") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
            label = { Text("Favoritos") }
        )
        if (esAdmin) {
            NavigationBarItem(
                selected = rutaActual == "admin",
                onClick = { if (rutaActual != "admin") onNavigate("admin") },
                icon = { Icon(Icons.Default.Settings, contentDescription = "Admin") },
                label = { Text("Admin") }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    esAdmin: Boolean = false,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val orden by viewModel.orden.collectAsStateWithLifecycle()
    val estadoLista = remember(orden) { LazyListState() }
    var menuOrdenAbierto by rememberSaveable { mutableStateOf(false) }

    val topBarColor = PokedexRed

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.background(Brush.horizontalGradient(listOf(Color(0xFFAA1720), Color(0xFFE92B27), Color(0xFFF07825)))),
                title = {
                    Text(
                        text = "Pokémon de Fuego",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                ),
                actions = {
                    if (!esAdmin) {
                        IconButton(onClick = onCerrarSesion) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Cerrar Sesión",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.sincronizar() },
                containerColor = PokedexYellow,
                contentColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Sincronizar"
                )
            }
        },
        bottomBar = {
            MiBottomNavBar(
                rutaActual = "lista",
                onNavigate = onNavigate,
                esAdmin = esAdmin
            )
        },
        containerColor = Color.Transparent,
        modifier = modifier.fuegoFondo()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = viewModel::cambiarBusqueda,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text("Buscar por nombre o número") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        IconButton(onClick = { viewModel.cambiarBusqueda("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Borrar búsqueda")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFFAF4),
                    unfocusedContainerColor = Color(0xFFFFFAF4)
                )
            )
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                TextButton(onClick = { menuOrdenAbierto = true }) {
                    Text(if (orden == OrdenLista.NUMERO) "Ordenar por: Número" else "Ordenar por: Nombre")
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(expanded = menuOrdenAbierto, onDismissRequest = { menuOrdenAbierto = false }) {
                    DropdownMenuItem(
                        text = { Text("Número (# menor a mayor)") },
                        onClick = { viewModel.cambiarOrden(OrdenLista.NUMERO); menuOrdenAbierto = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Nombre (A–Z)") },
                        onClick = { viewModel.cambiarOrden(OrdenLista.NOMBRE); menuOrdenAbierto = false }
                    )
                }
            }
            aviso?.let { mensajeAviso ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = PokedexYellow),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = mensajeAviso,
                        color = Color(0xFF5D4037),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }

            when (val state = uiState) {
                is UiState.Cargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = topBarColor,
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Cargando Pokémon...",
                                fontWeight = FontWeight.SemiBold,
                                color = topBarColor
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
                                modifier = Modifier.fillMaxWidth().padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error de Conexión",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = state.mensaje,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.refrescar() },
                                    colors = ButtonDefaults.buttonColors(containerColor = topBarColor)
                                ) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }
                is UiState.Exito -> {
                    val pokemonList = state.datos.items
                    if (pokemonList.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ElevatedCard(
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .padding(24.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = if (state.datos.listaOriginalVacia) "No hay elementos para mostrar" else "No se encontraron Pokémones",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = if (state.datos.listaOriginalVacia) MaterialTheme.typography.bodyLarge.fontSize else 18.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = if (state.datos.listaOriginalVacia) Modifier.padding(24.dp) else Modifier.fillMaxWidth().padding(24.dp)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            state = estadoLista,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(pokemonList, key = { it.id }) { item ->
                                ItemRow(
                                    item = item,
                                    onFavoritoClick = { viewModel.alternarFavorito(item.id) },
                                    onCardClick = { onItemClick(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemRow(
    item: Item,
    onFavoritoClick: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heartScale by animateFloatAsState(
        targetValue = if (item.esFavorito) 1.2f else 1.0f,
        label = "HeartScale"
    )
    val heartTint by animateColorAsState(
        targetValue = PokedexRed,
        label = "HeartTint"
    )

    ElevatedCard(
        onClick = onCardClick,
        shape = RoundedCornerShape(18.dp),
        colors = androidx.compose.material3.CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFF9F1)),
        elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.urlImagen.isNotEmpty()) {
                        AsyncImage(
                            model = item.urlImagen,
                            contentDescription = item.nombre,
                            modifier = Modifier.size(76.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "#${item.id.toString().padStart(3, '0')}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.nombre.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        maxLines = 2
                    )
                }
            }

            IconButton(onClick = onFavoritoClick) {
                Icon(
                    imageVector = if (item.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = heartTint,
                    modifier = Modifier.scale(heartScale)
                )
            }
        }
    }
}
