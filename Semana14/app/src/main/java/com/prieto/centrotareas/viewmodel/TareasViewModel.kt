package com.prieto.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import com.prieto.centrotareas.data.TareasRepository
import com.prieto.centrotareas.model.CargaUi
import com.prieto.centrotareas.model.PrimosUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // TODO Paso 1: fun tareaLargaMala()
    // TODO Paso 2: fun tareaLarga()
    // TODO Paso 3: progreso
    // TODO Paso 4: trabajo (Job), corriendo y cancelar()
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    // TODO Paso 5: fun contar(hasta: Int) y fun cancelarPrimos()
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    // TODO Paso 6: fun cargarUnaPorUna() y fun cargarALaVez()
    // TODO Paso 7: simularError y manejo de errores
}