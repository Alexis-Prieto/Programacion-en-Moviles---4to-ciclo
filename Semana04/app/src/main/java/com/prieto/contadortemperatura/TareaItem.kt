package com.prieto.contadortemperatura

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun TareaItem(
    tarea: Tarea,
    onToggleCompletada: (Int) -> Unit,
    onEliminar: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { onToggleCompletada(tarea.id) }
            )

            Text(
                text = tarea.descripcion,
                modifier = Modifier.weight(1f),
                textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
            )

            IconButton(onClick = { onEliminar(tarea.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}