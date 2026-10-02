package com.prieto.tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecsupStoreApp() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var pantallaActual by remember { mutableStateOf("Inicio") }

    var productosFavoritosIds by remember { mutableStateOf(setOf<Int>()) }

    val listaProductos = listOf(
        Producto(1, "Laptop Gamer", 4500.0, "Laptops"),
        Producto(2, "Mouse Gamer", 120.0, "Accesorios"),
        Producto(3, "Teclado", 280.0, "Accesorios"),
        Producto(4, "Audifonos", 89.0, "Audio"),
        Producto(5, "Smartwatch", 199.0, "Gadgets"),
        Producto(6, "Funda celular", 25.0, "Accesorios"),
        Producto(7, "Memoria USB", 45.0, "Accesorios"),
        Producto(8, "Pad Mouse", 35.0, "Accesorios")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                opcionSeleccionada = pantallaActual,
                cantidadFavoritos = productosFavoritosIds.size,
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
                    title = {
                        Column {
                            Text(
                                text = "TECSUP Store",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Mas vendidos",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF4A148C) // Morado oscuro idéntico a la maqueta
                    )
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
                        // Lista directa de productos sin categorías ni textos adicionales (como en la Foto 2)
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            items(listaProductos) { producto ->
                                val esFav = productosFavoritosIds.contains(producto.id)
                                TarjetaProducto(
                                    producto = producto,
                                    esFavorito = esFav,
                                    onToggleFavorito = {
                                        productosFavoritosIds = if (esFav) {
                                            productosFavoritosIds - producto.id
                                        } else {
                                            productosFavoritosIds + producto.id
                                        }
                                    }
                                )
                            }
                        }
                    }
                    "Mis pedidos" -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Sección: Mis pedidos")
                        }
                    }
                    "Favoritos" -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Productos Favoritos seleccionados: ${productosFavoritosIds.size}")
                        }
                    }
                    "Perfil" -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Sección: Perfil")
                        }
                    }
                }
            }
        }
    }
}