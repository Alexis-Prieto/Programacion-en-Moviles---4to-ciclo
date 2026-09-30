package com.prieto.tecsupstore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun TecsupStoreApp() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer()
        }
    ) {
        Scaffold { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                TarjetaProducto(
                    producto = Producto(1, "Laptop Gamer", 4500.0, "Laptops")
                )
            }
        }
    }
}