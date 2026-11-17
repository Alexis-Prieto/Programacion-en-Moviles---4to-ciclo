
package com.prieto.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prieto.campusgps.model.UbicacionUi
import com.prieto.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val estado by vm.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { vm.buscar() },
            enabled = estado !is UbicacionUi.Buscando
        ) {
            Text("Obtener mi ubicación")
        }

        when (val actual = estado) {
            UbicacionUi.Inicial -> {
                Text("Pulsa el botón para obtener tu ubicación")
            }

            UbicacionUi.Buscando -> {
                Text("Buscando ubicación...")
            }

            is UbicacionUi.Lista -> {
                Text("Latitud: ${actual.posicion.latitud}")
                Text("Longitud: ${actual.posicion.longitud}")
            }

            is UbicacionUi.Error -> {
                Text(actual.mensaje)
            }
        }
    }
}
