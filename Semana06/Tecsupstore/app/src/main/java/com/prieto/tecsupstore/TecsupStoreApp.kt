package com.prieto.tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecsupStoreApp() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var pantallaActual by remember { mutableStateOf("Inicio") }
    val listaCategorias = listOf("Todas", "Laptops", "Accesorios")
    val listaProductos = listOf(
        Producto(1, "Laptop Gamer", 4500.0, "Laptops"),
        Producto(2, "Mouse Gamer", 120.0, "Accesorios"),
        Producto(3, "Teclado", 280.0, "Accesorios")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                opcionSeleccionada = pantallaActual,
                onOpcionSeleccionada = { nuevaPantalla ->
                    pantallaActual = nuevaPantalla
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(pantallaActual) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                when (pantallaActual) {
                    "Inicio" -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "Categorías",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
                            )
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(listaCategorias) { categoria ->
                                    FilterChip(
                                        selected = false,
                                        onClick = { },
                                        label = { Text(categoria) }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Productos destacados",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                            )
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(listaProductos) { producto ->
                                    TarjetaProducto(producto = producto)
                                }
                            }
                        }
                    }
                    "Categorías" -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Sección de Categorías")
                        }
                    }
                    "Carrito" -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tu Carrito de Compras")
                        }
                    }
                }
            }
        }
    }
}