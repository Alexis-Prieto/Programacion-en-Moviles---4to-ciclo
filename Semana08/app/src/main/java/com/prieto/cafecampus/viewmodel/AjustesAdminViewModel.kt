package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AjustesAdminViewModel(
    private val repository: CafeRepository,
    private val store: PreferenciasStore
) : ViewModel() {

    val modoOscuro: StateFlow<Boolean> = store.modoOscuro
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val estaRecibiendoPedidos: StateFlow<Boolean> = store.recibirPedidos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun cambiarPerfil(perfil: String, onResult: () -> Unit) {
        viewModelScope.launch {
            store.guardarPerfil(perfil)
            onResult()
        }
    }

    fun cambiarModoOscuro(activo: Boolean) {
        viewModelScope.launch {
            store.guardarModoOscuro(activo)
        }
    }

    fun cambiarRecibirPedidos(activo: Boolean) {
        viewModelScope.launch {
            store.guardarRecibirPedidos(activo)
        }
    }

    fun cerrarSesion(onResult: () -> Unit) {
        viewModelScope.launch {
            store.cerrarSesion()
            onResult()
        }
    }
}