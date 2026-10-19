package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val repository: CafeRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    val usuarioId: StateFlow<Int> = preferenciasStore.usuarioId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val nombreUsuario: StateFlow<String> = preferenciasStore.nombreUsuario
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val correoUsuario: StateFlow<String> = preferenciasStore.correoUsuario
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val fotoPerfilUri: StateFlow<String?> = preferenciasStore.fotoPerfilUri
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val perfilActivo: StateFlow<String> = preferenciasStore.perfilActivo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Estudiante")

    fun actualizarDatosPersonales(nuevoNombre: String, nuevoCorreo: String) {
        viewModelScope.launch {
            val id = usuarioId.value
            if (id != 0) {
                repository.actualizarDatosUsuario(id, nuevoNombre, nuevoCorreo)
            }
            preferenciasStore.guardarDatosUsuario(nuevoNombre, nuevoCorreo)
        }
    }
    fun guardarFotoPerfil(uriString: String) {
        viewModelScope.launch {
            val id = usuarioId.value
            if (id != 0) {
                preferenciasStore.guardarFotoPerfilUri(uriString, id)
            }
        }
    }
    fun cerrarSesion(menuViewModel: MenuViewModel, onExito: () -> Unit) {
        viewModelScope.launch {
            preferenciasStore.cerrarSesion()
            menuViewModel.vaciar()
            onExito()
        }
    }
}