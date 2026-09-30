package com.prieto.tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    opcionSeleccionada: String,
    onOpcionSeleccionada: (String) -> Unit
) {
    ModalDrawerSheet {
        Text(
            text = "TECSUP Store",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") },
            selected = opcionSeleccionada == "Inicio",
            onClick = { onOpcionSeleccionada("Inicio") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.List, contentDescription = null) },
            label = { Text("Categorías") },
            selected = opcionSeleccionada == "Categorías",
            onClick = { onOpcionSeleccionada("Categorías") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
            label = { Text("Carrito") },
            selected = opcionSeleccionada == "Carrito",
            onClick = { onOpcionSeleccionada("Carrito") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}