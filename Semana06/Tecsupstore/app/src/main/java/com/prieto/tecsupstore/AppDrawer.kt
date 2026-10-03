package com.prieto.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    opcionSeleccionada: String,
    onOpcionSeleccionada: (String) -> Unit
) {
    val opciones = listOf("Inicio", "Categorías", "Carrito")

    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        drawerTonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE8DEF8), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AP",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A148C),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Alexis Prieto",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1D1B20)
                )
                Text(
                    text = "alexis.prieto@tecsup.edu.pe",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        opciones.forEach { opcion ->
            val esSeleccionado = (opcion == opcionSeleccionada)

            val icono = when (opcion) {
                "Inicio" -> Icons.Default.Home
                "Categorías" -> Icons.Default.List
                "Carrito" -> Icons.Default.ShoppingCart
                else -> Icons.Default.Home
            }

            NavigationDrawerItem(
                label = {
                    Text(
                        text = opcion,
                        fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = esSeleccionado,
                onClick = { onOpcionSeleccionada(opcion) },
                icon = {
                    Icon(
                        imageVector = icono,
                        contentDescription = opcion
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color(0xFFF3EDF7),
                    selectedIconColor = Color(0xFF4A148C),
                    selectedTextColor = Color(0xFF4A148C),
                    unselectedContainerColor = Color.Transparent,
                    unselectedIconColor = Color(0xFF1D1B20),
                    unselectedTextColor = Color(0xFF1D1B20)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
        }
    }
}