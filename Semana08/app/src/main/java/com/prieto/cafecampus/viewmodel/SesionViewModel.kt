package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.local.entity.Usuario
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: CafeRepository,
    private val prefsStore: PreferenciasStore)
    : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    val modoOscuro: StateFlow<Boolean> = prefsStore.modoOscuro.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    val perfilActivo: StateFlow<String> = prefsStore.perfilActivo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    fun login(correo: String, clave: String, rolSeleccionado: String, onSuccess: (String) -> Unit) {
        val correoTrim = correo.trim()
        val claveTrim = clave.trim()

        if (correoTrim.isEmpty() || claveTrim.isEmpty()) {
            _error.value = "Por favor, ingresa correo y contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repository.login(correoTrim, claveTrim)
            if (usuario == null) {
                _error.value = "Correo o contraseña incorrectos"
            } else {
                val rolEsperado = if (rolSeleccionado == "Estudiante") "CLIENTE" else "ADMIN"
                if (usuario.rol.uppercase() != rolEsperado) {
                    _error.value = "El usuario no coincide con el rol de $rolSeleccionado"
                } else {
                    _usuarioActual.value = usuario
                    _error.value = null
                    prefsStore.guardarPerfil(usuario.rol)
                    onSuccess(usuario.rol)
                }
            }
        }
    }
    fun limpiarError() {
        _error.value = null
    }
    fun cerrarSesion() {
        viewModelScope.launch {
            prefsStore.cerrarSesion()
            _usuarioActual.value = null
        }
    }
    class Factory(
        private val repository: CafeRepository,
        private val prefsStore: PreferenciasStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
                return SesionViewModel(repository, prefsStore) as T
            }
            throw IllegalArgumentException("ViewModel desconocido")
        }
    }
}