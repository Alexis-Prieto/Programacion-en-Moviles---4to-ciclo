package com.prieto.pokeapp.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.prieto.pokeapp.data.auth.AuthRepository
import com.prieto.pokeapp.domain.model.Sesion
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {

    var correo by mutableStateOf("")
        private set

    var clave by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow<UiState<Sesion>?>(null)
    val uiState: StateFlow<UiState<Sesion>?> = _uiState.asStateFlow()

    fun onCorreoChanged(nuevoCorreo: String) {
        correo = nuevoCorreo
    }

    fun onClaveChanged(nuevaClave: String) {
        clave = nuevaClave
    }

    fun iniciarSesion() {
        val correoClean = correo.trim()
        val claveClean = clave.trim()

        if (correoClean.isEmpty() || claveClean.isEmpty()) {
            _uiState.value = UiState.Error("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Cargando

            authRepo.cerrarSesion()

            val resultado = authRepo.iniciarSesion(correoClean, claveClean)
            resultado
                .onSuccess { sesion ->
                    _uiState.value = UiState.Exito(sesion)
                }
                .onFailure { error ->
                    val mensaje = when (error) {
                        is FirebaseNetworkException, is IOException -> "Error de conexión. Revisa tu internet."
                        else -> "Correo o contraseña incorrectos."
                    }
                    _uiState.value = UiState.Error(mensaje)
                }
        }
    }

    fun resetearEstado() {
        _uiState.value = null
        correo = ""
        clave = ""
    }
}
