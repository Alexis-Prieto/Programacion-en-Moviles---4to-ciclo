package com.prieto.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.prieto.campusgps.model.UbicacionUi
import com.prieto.campusgps.util.formatearDistancia
import com.prieto.campusgps.viewmodel.MapaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val azulCampus = Color(0xFF1976A3)
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugaresFiltrados.collectAsState()
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val radioSeleccionado by vm.radioSeleccionado.collectAsState()
    val lugaresDentroDelRadio by vm.lugaresDentroDelRadio.collectAsState()
    val estadoLista = rememberLazyListState()
    val alcance = rememberCoroutineScope()

    val contexto = LocalContext.current

    fun tienePermisoUbicacion(): Boolean =
        ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                contexto,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

    var permisoUbicacionConcedido by remember(contexto) {
        mutableStateOf(tienePermisoUbicacion())
    }
    fun actualizarPermisoUbicacion() {
        val permisoActual = tienePermisoUbicacion()
        val permisoRevocado = permisoUbicacionConcedido && !permisoActual
        permisoUbicacionConcedido = permisoActual
        if (!permisoActual && (
                permisoRevocado || vm.estado.value is UbicacionUi.Lista ||
                    vm.estado.value is UbicacionUi.Buscando
                )) {
            vm.permisoDenegado()
        }
    }
    val propietarioCicloVida = LocalLifecycleOwner.current
    DisposableEffect(propietarioCicloVida, contexto, vm) {
        val observador = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {
                actualizarPermisoUbicacion()
            }
        }
        propietarioCicloVida.lifecycle.addObserver(observador)
        onDispose {
            propietarioCicloVida.lifecycle.removeObserver(observador)
        }
    }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        actualizarPermisoUbicacion()
        if (permisoUbicacionConcedido) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }
    fun alPulsar() {
        actualizarPermisoUbicacion()
        if (permisoUbicacionConcedido) {
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
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = Color.White,
        contentColor = Color(0xFF263849),
        topBar = {
            TopAppBar(
                title = {
                    Text("CampusGPS")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = azulCampus,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("Todos", "Estudio", "Comida", "Eventos", "Servicios")) { categoria ->
                    FilterChip(
                        selected = categoriaSeleccionada == categoria,
                        onClick = { vm.seleccionarCategoria(categoria) },
                        label = { Text(categoria) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = azulCampus,
                            selectedContainerColor = azulCampus,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item { Text("Radio:") }
                items(listOf(100, 200, 500)) { radio ->
                    FilterChip(
                        selected = radioSeleccionado == radio,
                        onClick = { vm.seleccionarRadio(radio) },
                        label = { Text("$radio m") },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = azulCampus,
                            selectedContainerColor = azulCampus,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            MapaUbicacion(
                estado = if (permisoUbicacionConcedido) estado else UbicacionUi.Inicial,
                permisoUbicacionConcedido = permisoUbicacionConcedido,
                radioMetros = radioSeleccionado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercanoId = masCercano?.id,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { alPulsar() },
                enabled = estado !is UbicacionUi.Buscando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = azulCampus,
                    contentColor = Color.White
                )
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
                    if (permisoUbicacionConcedido) {
                        Text("Latitud: ${actual.posicion.latitud}")
                        Text("Longitud: ${actual.posicion.longitud}")
                    }
                }

                is UbicacionUi.Error -> {
                    MensajeError(
                        error = actual,
                        onReintentar = { alPulsar() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            OutlinedButton(
                onClick = {
                    vm.seleccionarMasCercano()
                    val indice = vm.lugaresFiltrados.value.indexOfFirst {
                        it.lugar.id == vm.seleccionado.value
                    }
                    if (indice >= 0) {
                        alcance.launch {
                            estadoLista.animateScrollToItem(indice)
                        }
                    }
                },
                enabled = masCercano != null,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = azulCampus
                ),
                border = BorderStroke(
                    1.dp,
                    if (masCercano != null) {
                        azulCampus
                    } else {
                        Color.LightGray
                    }
                )
            ) {
                Text("Ir al más cercano")
            }

            masCercano?.let { lugar ->
                val metros = lugares.firstOrNull {
                    it.lugar.id == lugar.id
                }?.metros

                if (metros != null) {
                    Text(
                        text = "Más cercano: ${lugar.nombre} (${formatearDistancia(metros)})"
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            ListaLugares(
                lugares = lugares,
                lugaresDentroDelRadio = if (permisoUbicacionConcedido) lugaresDentroDelRadio else emptySet(),
                estadoLista = estadoLista,
                seleccionado = seleccionado,
                onClick = { id ->
                    vm.seleccionar(id)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}
