package com.prieto.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.prieto.campusgps.model.TipoError
import com.prieto.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val azulCampus = Color(0xFF1976A3)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = error.mensaje,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            when (error.tipo) {
                TipoError.SIN_PERMISO -> {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            ).apply {
                                data = Uri.fromParts(
                                    "package",
                                    contexto.packageName,
                                    null
                                )
                            }

                            contexto.startActivity(intent)
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = azulCampus
                        ),
                        border = BorderStroke(1.dp, azulCampus)
                    ) {
                        Text("Abrir ajustes")
                    }
                }
                TipoError.GPS_APAGADO -> {
                    OutlinedButton(
                        onClick = onReintentar,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = azulCampus
                        ),
                        border = BorderStroke(1.dp, azulCampus)
                    ) {
                        Text("Reintentar")
                    }
                }
                TipoError.SIN_PLAY_SERVICES -> {
                }
                TipoError.OTRO -> {
                    OutlinedButton(
                        onClick = onReintentar,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = azulCampus
                        ),
                        border = BorderStroke(1.dp, azulCampus)
                    ) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}
