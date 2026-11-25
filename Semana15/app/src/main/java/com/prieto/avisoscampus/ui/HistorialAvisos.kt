package com.prieto.avisoscampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.avisoscampus.model.Aviso
import com.prieto.avisoscampus.ui.theme.*

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {

    Tarjeta("Historial") {
        if (avisos.isEmpty()) {
            Text(
                text = "Sin avisos todavía",
                color = TextoSecundario
            )
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                avisos.take(20).forEach { aviso ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFEEF2F6),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment =
                                    Alignment.CenterVertically,
                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    color = Color(0xFFDCEAF4),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = aviso.origen.texto,
                                        color = AzulPrincipal,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                                Text(
                                    text = aviso.hora,
                                    color = TextoSecundario,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = aviso.titulo,
                                color = TextoPrincipal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = aviso.texto,
                                color = TextoSecundario,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}