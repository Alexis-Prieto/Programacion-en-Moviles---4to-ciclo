package com.prieto.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.prieto.campusgps.model.LugarConDistancia
import com.prieto.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    // TODO Paso 3: LaunchedEffect(estado), animar la cámara

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara
    ) {
        // TODO Paso 3: marcador de ubicación actual

        // TODO Paso 4: marcadores de los lugares
    }
}
