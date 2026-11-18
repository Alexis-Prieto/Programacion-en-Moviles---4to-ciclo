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
    val fondoError = Color(0xFFFBE4E4)
    val textoError = Color(0xFFAA3535)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = fondoError
            )
        ) {
            Text(
                text = error.mensaje,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = textoError,
                textAlign = TextAlign.Center
            )
        }
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
            TipoError.GPS_APAGADO,
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
            TipoError.SIN_PLAY_SERVICES -> {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        ).apply {
                            data = Uri.fromParts(
                                "package",
                                "com.google.android.gms",
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
                    Text("Revisar Play Services")
                }
            }
        }
    }
}
