package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface RegistroState {
    object Idle : RegistroState
    object Loading : RegistroState
    object Success : RegistroState
    data class Error(val mensaje: String) : RegistroState
}

class RegistroViewModel(
    private val repository: CafeRepository,
    private val prefsStore: PreferenciasStore
) : ViewModel() {

    var nombre = MutableStateFlow("")
    var correo = MutableStateFlow("")
    var clave = MutableStateFlow("")
    var confirmarClave = MutableStateFlow("")

    private val _state = MutableStateFlow<RegistroState>(RegistroState.Idle)
    val state: StateFlow<RegistroState> = _state

    fun registrar() {
        val nom = nombre.value.trim()
        val email = correo.value.trim()
        val pass = clave.value.trim()
        val confirm = confirmarClave.value.trim()

        if (nom.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            _state.value = RegistroState.Error("Todos los campos son obligatorios")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.value = RegistroState.Error("El correo electrónico no es válido")
            return
        }

        if (pass != confirm) {
            _state.value = RegistroState.Error("Las contraseñas no coinciden")
            return
        }
        if (pass.length < 6) {
            _state.value = RegistroState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _state.value = RegistroState.Loading
            try {
                if (repository.existeCorreo(email)) {
                    _state.value = RegistroState.Error("El correo ya se encuentra registrado")
                } else {
                    val result = repository.registrarEstudiante(nom, email, pass)
                    if (result > 0) {
                        prefsStore.guardarSesionUsuario(
                            id = result.toInt(),
                            nombre = nom,
                            correo = email,
                            rol = "ESTUDIANTE"
                        )
                        _state.value = RegistroState.Success
                    } else {
                        _state.value = RegistroState.Error("Error al registrar el estudiante")
                    }
                }
            } catch (e: Exception) {
                _state.value = RegistroState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    fun resetState() {
        _state.value = RegistroState.Idle
    }

    class Factory(
        private val repository: CafeRepository,
        private val prefsStore: PreferenciasStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegistroViewModel::class.java)) {
                return RegistroViewModel(repository, prefsStore) as T
            }
            throw IllegalArgumentException("ViewModel desconocido")
        }
    }
}