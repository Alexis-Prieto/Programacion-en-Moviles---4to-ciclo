package com.prieto.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.campusgps.data.LugaresRepository
import com.prieto.campusgps.data.UbicacionRepository
import com.prieto.campusgps.model.Lugar
import com.prieto.campusgps.model.LugarConDistancia
import com.prieto.campusgps.model.Posicion
import com.prieto.campusgps.model.TipoError
import com.prieto.campusgps.model.UbicacionUi
import com.prieto.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()
    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()
    private val _categoriaSeleccionada = MutableStateFlow("Todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()
    private val _lugaresFiltrados = MutableStateFlow(_lugares.value)
    val lugaresFiltrados: StateFlow<List<LugarConDistancia>> = _lugaresFiltrados.asStateFlow()
    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()
    private val _radioSeleccionado = MutableStateFlow(100)
    val radioSeleccionado: StateFlow<Int> = _radioSeleccionado.asStateFlow()
    val lugaresDentroDelRadio: StateFlow<Set<Int>> = combine(
        lugares, radioSeleccionado, estado
    ) { lista, radio, ubicacion ->
        if (ubicacion is UbicacionUi.Lista) {
            lista.filter { it.metros?.let { metros -> metros <= radio } == true }
                .map { it.lugar.id }
                .toSet()
        } else {
            emptySet()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptySet()
    )
    fun seleccionarRadio(radio: Int) {
        if (radio == 100 || radio == 200 || radio == 500) {
            _radioSeleccionado.value = radio
        }
    }
    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
        actualizarFiltro()
    }
    private fun actualizarFiltro() {
        val categoria = _categoriaSeleccionada.value
        val visibles = _lugares.value.filter {
            categoria == "Todos" || it.lugar.categoria == categoria
        }
        if (visibles.none { it.lugar.id == _seleccionado.value }) {
            _seleccionado.value = null
        }
        _lugaresFiltrados.value = visibles
    }
    fun buscar() {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure {
                    _estado.value = errorDe(it)
                }
        }
    }
    private fun actualizarDistancias(pos: Posicion) {
        val lista = lugaresRepo.obtenerLugares()
            .map { lugar ->
                LugarConDistancia(
                    lugar,
                    distanciaMetros(pos, lugar)
                )
            }
            .sortedBy { it.metros }

        _lugares.value = lista
        actualizarFiltro()

        lista.forEach {
            Log.d(
                "CampusGPS",
                "${it.lugar.nombre}: ${it.metros} m"
            )
        }
    }
    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
    }
    fun seleccionar(id: Int?) {
        _seleccionado.value = id?.takeIf { candidato ->
            _lugaresFiltrados.value.any { it.lugar.id == candidato }
        }
    }
    fun seleccionarMasCercano() {
        seleccionar(_lugaresFiltrados.value.firstOrNull { it.metros != null }?.lugar?.id)
    }
    val masCercano: StateFlow<Lugar?> = lugaresFiltrados
        .map { lista ->
            lista.firstOrNull { it.metros != null }?.lugar
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )

        is IllegalStateException -> UbicacionUi.Error(
            "Activa el GPS e intenta de nuevo",
            TipoError.GPS_APAGADO
        )

        else -> UbicacionUi.Error(
            e.message ?: "No se pudo obtener la ubicación",
            TipoError.OTRO
        )
    }
}
