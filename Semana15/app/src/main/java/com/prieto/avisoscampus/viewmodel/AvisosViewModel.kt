package com.prieto.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.avisoscampus.data.AvisosRepository
import com.prieto.avisoscampus.model.Aviso
import com.prieto.avisoscampus.model.Origen
import com.prieto.avisoscampus.model.PermisoUi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

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

    private var trabajo: Job? = null
    private val _faltan = MutableStateFlow<Int?>(null)

    val faltan: StateFlow<Int?> = _faltan.asStateFlow()

    private val _escuchandoAvion = MutableStateFlow(false)

    val escuchandoAvion: StateFlow<Boolean> =
        _escuchandoAvion.asStateFlow()
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
    fun recordarEn(segundos: Int = 10) {

        if (trabajo?.isActive == true) return
        trabajo = viewModelScope.launch {

            try {
                for (i in segundos downTo 1) {

                    _faltan.value = i

                    delay(1000)
                }
                val aviso = repo.registrar(
                    titulo = "Recordatorio",
                    texto = "Revisa tu laboratorio pendiente",
                    origen = Origen.RECORDATORIO
                )
                _eventos.emit(aviso)

            } finally {
                _faltan.value = null
            }
        }
    }
    fun cancelarRecordatorio() {
        trabajo?.cancel()
    }

    fun cambiarEscucha(valor: Boolean) {
        _escuchandoAvion.value = valor
    }
    fun alCambiarModoAvion(activo: Boolean) {

        if (!_escuchandoAvion.value) return

        viewModelScope.launch {
            val aviso = repo.registrar(
                titulo = "Modo avión",
                texto = if (activo) {
                    "Activado"
                } else {
                    "Desactivado"
                },
                origen = Origen.MODO_AVION
            )

            _eventos.emit(aviso)
        }
    }
    // TODO Paso 8: noSePudoMostrar()
}