package com.prieto.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.avisoscampus.data.AvisosRepository
import com.prieto.avisoscampus.model.Aviso
import com.prieto.avisoscampus.model.Origen
import com.prieto.avisoscampus.model.PermisoUi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AvisosViewModel(
    private val repo: AvisosRepository
) : ViewModel() {

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    private val _permiso =
        MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> =
        _permiso.asStateFlow()

    private val _mensaje =
        MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> =
        _mensaje.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        _permiso.value = if (concedido) {
            PermisoUi.Concedido
        } else {
            PermisoUi.Denegado
        }
    }

    fun avisarAhora() {
        viewModelScope.launch {
            val aviso = repo.registrar(
                titulo = "Tarea lista",
                texto = "Tu entrega fue registrada",
                origen = Origen.BOTON
            )
            _eventos.emit(aviso)
        }
    }
    // TODO Paso 5: faltan, recordarEn()
    // y cancelarRecordatorio()

    // TODO Paso 6: escuchandoAvion,
    // cambiarEscucha() y alCambiarModoAvion()

    // TODO Paso 8: noSePudoMostrar()
}