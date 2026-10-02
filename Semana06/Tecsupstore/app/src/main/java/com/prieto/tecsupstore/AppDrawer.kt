package com.prieto.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    opcionSeleccionada: String,
    cantidadFavoritos: Int = 0,
    onOpcionSeleccionada: (String) -> Unit
) {
    ModalDrawerSheet {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(24.dp)
        ) {
            Column {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil de usuario",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "TECSUP Store",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "alexis.prieto@tecsup.edu.pe",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Destino 1: Inicio
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") },
            selected = opcionSeleccionada == "Inicio",
            onClick = { onOpcionSeleccionada("Inicio") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        // Destino 2: Mis pedidos
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.ShoppingBag, contentDescription = null) },
            label = { Text("Mis pedidos") },
            selected = opcionSeleccionada == "Mis pedidos",
            onClick = { onOpcionSeleccionada("Mis pedidos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        // Destino 3: Favoritos (con Badge reactivo de Fase 2)
        NavigationDrawerItem(
            icon = {
                BadgedBox(
                    badge = {
                        if (cantidadFavoritos > 0) {
                            Badge { Text("$cantidadFavoritos") }
                        }
                    }
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                }
            },
            label = { Text("Favoritos") },
            selected = opcionSeleccionada == "Favoritos",
            onClick = { onOpcionSeleccionada("Favoritos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        // Destino 4: Perfil
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Perfil") },
            selected = opcionSeleccionada == "Perfil",
            onClick = { onOpcionSeleccionada("Perfil") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}