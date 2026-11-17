package com.prieto.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prieto.campusgps.model.LugarConDistancia
import com.prieto.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val azulCampus = Color(0xFF1976A3)
    val fondoNormal = Color(0xFFF0F3F6)
    val fondoSeleccionado = Color(0xFFD8EAFB)
    val colorNombre = Color(0xFF263849)
    val colorCategoria = Color(0xFF657584)

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = lugares,
            key = { it.lugar.id }
        ) { item ->

            val estaSeleccionado = seleccionado == item.lugar.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick(item.lugar.id)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (estaSeleccionado) {
                        fondoSeleccionado
                    } else {
                        fondoNormal
                    }
                ),
                border = if (estaSeleccionado) {
                    BorderStroke(
                        2.dp,
                        azulCampus
                    )
                } else {
                    null
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                azulCampus,
                                CircleShape
                            )
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = item.lugar.nombre,
                            fontWeight = FontWeight.Bold,
                            color = colorNombre
                        )

                        Text(
                            text = item.lugar.categoria,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorCategoria
                        )
                    }
                    Text(
                        text = item.metros?.let {
                            formatearDistancia(it)
                        } ?: "Sin calcular",
                        color = if (item.metros != null) {
                            azulCampus
                        } else {
                            colorCategoria
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
