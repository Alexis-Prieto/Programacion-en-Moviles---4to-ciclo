package com.prieto.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.prieto.campusgps.model.LugarConDistancia
import com.prieto.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    radioMetros: Int,
    permisoUbicacionConcedido: Boolean,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val posicion = LatLng(
                estado.posicion.latitud,
                estado.posicion.longitud
            )

            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    posicion,
                    16f
                )
            )
        }
    }
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull {
            it.lugar.id == seleccionado
        }?.lugar

        if (lugar != null) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        lugar.latitud,
                        lugar.longitud
                    ),
                    17f
                )
            )
        }
    }
    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        properties = MapProperties(isMyLocationEnabled = permisoUbicacionConcedido),
        uiSettings = MapUiSettings(myLocationButtonEnabled = permisoUbicacionConcedido)
    ) {
        if (permisoUbicacionConcedido && estado is UbicacionUi.Lista) {
            Circle(
                center = LatLng(
                    estado.posicion.latitud,
                    estado.posicion.longitud
                ),
                radius = radioMetros.toDouble(),
                strokeColor = Color(0xFF1976A3),
                fillColor = Color(0xFF1976A3).copy(alpha = 0.15f),
                strokeWidth = 3f
            )
        }
        lugares.forEach { lugarConDistancia ->
            val lugar = lugarConDistancia.lugar

            key(lugar.id) {
                Marker(
                    state = rememberUpdatedMarkerState(
                        position = LatLng(
                            lugar.latitud,
                            lugar.longitud
                        )
                    ),
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (lugar.id == masCercanoId) {
                            BitmapDescriptorFactory.HUE_GREEN
                        } else {
                            BitmapDescriptorFactory.HUE_AZURE
                        }
                    )
                )
            }
        }
    }
}
