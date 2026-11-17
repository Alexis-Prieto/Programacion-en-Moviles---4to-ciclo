
package com.prieto.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.prieto.campusgps.model.UbicacionUi
import com.prieto.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val estado by vm.estado.collectAsState()
    val contexto = LocalContext.current

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }
    fun alPulsar() {
        val permisoFino = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permisoAproximado = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (
            permisoFino == PackageManager.PERMISSION_GRANTED ||
            permisoAproximado == PackageManager.PERMISSION_GRANTED
        ) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { alPulsar() },
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
