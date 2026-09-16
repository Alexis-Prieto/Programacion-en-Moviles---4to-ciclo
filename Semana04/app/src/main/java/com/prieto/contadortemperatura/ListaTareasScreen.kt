package com.prieto.contadortemperatura

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.contadortemperatura.ui.theme.ContadorTemperaturaTheme

@Composable
fun ListaTareasScreen(modifier: Modifier = Modifier) {
    var nuevaTarea by remember { mutableStateOf("") }
    var listaTareas by remember { mutableStateOf(listOf<Tarea>()) }
    var siguienteId by remember { mutableStateOf(1) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Lista de tareas - Tecsup",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A5E)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nuevaTarea,
            onValueChange = { nuevaTarea = it },
            label = { Text("¿Qué tarea tienes pendiente?") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (nuevaTarea.isNotBlank()) {
                    listaTareas = listaTareas + Tarea(siguienteId, nuevaTarea)
                    siguienteId++
                    nuevaTarea = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A5E))
        ) {
            Text("Agregar tarea")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Total de tareas: ${listaTareas.size}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(listaTareas, key = { it.id }) { tarea ->
                TareaItem(
                    tarea = tarea,
                    onToggleCompletada = { id ->
                        listaTareas = listaTareas.map {
                            if (it.id == id) it.copy(completada = !it.completada) else it
                        }
                    },
                    onEliminar = { id ->
                        listaTareas = listaTareas.filter { it.id != id }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaTareasScreenPreview() {
    ContadorTemperaturaTheme {
        ListaTareasScreen()
    }
}