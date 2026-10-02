package com.prieto.tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppDrawer(
    opcionSeleccionada: String,
    cantidadFavoritos: Int = 0,
    onOpcionSeleccionada: (String) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Encabezado horizontal: Avatar "AP" + Nombre y Correo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Círculo lavanda con iniciales "AP"
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color(0xFFE8DEF8)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "AP",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A148C),
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Datos del usuario
                Column {
                    Text(
                        text = "Alexis Prieto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D1B20)
                    )
                    Text(
                        text = "alexis.prieto@tecsup.edu.pe",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF79747E)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = Color(0xFFECE6F0), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Opciones del menú con círculos delineados
            val opciones = listOf(
                "Inicio",
                "Mis pedidos",
                "Favoritos",
                "Perfil",
                "Cerrar sesion"
            )
            opciones.forEach { opcion ->
                val esSeleccionado = opcionSeleccionada == opcion

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = opcion,
                            fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal,
                            color = if (esSeleccionado) Color(0xFF4A148C) else Color(0xFF1D1B20)
                        )
                    },
                    selected = esSeleccionado,
                    onClick = { onOpcionSeleccionada(opcion) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (esSeleccionado) Color(0xFF4A148C) else Color(0xFF49454F)
                        )
                    },
                    // Badge con contador para la opción "Favoritos"
                    badge = {
                        if (opcion == "Favoritos" && cantidadFavoritos > 0) {
                            Badge(
                                containerColor = Color(0xFF4A148C),
                                contentColor = Color.White
                            ) {
                                Text(text = cantidadFavoritos.toString())
                            }
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFFF3EDF7), // Lavanda suave en selección
                        unselectedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}