package com.prieto.pokeapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.data.auth.AuthRepository
import com.prieto.pokeapp.data.auth.ErrorConexionAuthException
import com.prieto.pokeapp.data.auth.ErrorRolException
import com.prieto.pokeapp.data.auth.TiempoAgotadoAuthException
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

    var correo = ""
        private set

    var clave = ""
        private set

    private val _uiState = MutableStateFlow<UiState<Sesion?>>(UiState.Exito(null))
    val uiState: StateFlow<UiState<Sesion?>> = _uiState.asStateFlow()

    private val _sesion = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val sesion: StateFlow<UiState<Sesion?>> = _sesion.asStateFlow()

    init {
        viewModelScope.launch {
            _sesion.value = try {
                UiState.Exito(authRepo.sesionActual())
            } catch (e: Exception) {
                UiState.Error("No se pudo recuperar la sesión.")
            }
        }
    }

    fun aceptarSesion(sesion: Sesion) {
        _sesion.value = UiState.Exito(sesion)
    }

    fun cerrarSesion() {
        authRepo.cerrarSesion()
        _sesion.value = UiState.Exito(null)
    }

    fun reintentarSesion() {
        _sesion.value = UiState.Cargando
        viewModelScope.launch {
            _sesion.value = try {
                UiState.Exito(authRepo.sesionActual())
            } catch (e: Exception) {
                UiState.Error("No se pudo recuperar la sesión.")
            }
        }
    }

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

            val resultado = authRepo.iniciarSesion(correoClean, claveClean)
            resultado
                .onSuccess { sesion ->
                    _uiState.value = UiState.Exito(sesion)
                }
                .onFailure { error ->
                    val mensaje = when (error) {
                        is ErrorRolException -> "No se pudo verificar tu rol. Revisa tu conexión e inténtalo de nuevo."
                        is TiempoAgotadoAuthException -> "El inicio de sesión tardó demasiado. Revisa tu conexión e inténtalo de nuevo."
                        is ErrorConexionAuthException, is IOException -> "Error de conexión. Revisa tu internet."
                        else -> "Correo o contraseña incorrectos."
                    }
                    _uiState.value = UiState.Error(mensaje)
                }
        }
    }

    fun resetearEstado() {
        _uiState.value = UiState.Exito(null)
        correo = ""
        clave = ""
    }
}
