package com.prieto.pokeapp.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.data.repository.ItemRepository
import com.prieto.pokeapp.notification.Notificador
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.prieto.pokeapp.data.repository.ErrorServidorException
import java.io.IOException

class AdminViewModel(
    private val itemRepository: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Exito(""))
    val uiState: StateFlow<UiState<String>> = _uiState.asStateFlow()

    val totalGuardados: StateFlow<Int> = itemRepository.items
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalFavoritos: StateFlow<Int> = itemRepository.favoritos
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun sincronizarDatos() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            val resultado = itemRepository.refrescar()
            resultado
                .onSuccess { n ->
                    notificador.mostrarNotificacion("PokeApp", "Se guardaron $n elementos")
                    _uiState.value = UiState.Exito("Sincronización completada ($n elementos)")
                }
                .onFailure { e ->
                    val mensaje = when (e) {
                        is IOException -> "No hay conexión a internet."
                        is ErrorServidorException -> if (e.codigo == 404) "No se encontró la información del Pokémon." else "El servidor no responde. Intenta más tarde."
                        else -> "Ocurrió un error inesperado."
                    }
                    _uiState.value = UiState.Error(mensaje)
                }
        }
    }

    fun sincronizar() = sincronizarDatos()

    fun limpiarCache() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                itemRepository.limpiarCache()
                _uiState.value = UiState.Exito("Caché limpiada correctamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Ocurrió un error inesperado.")
            }
        }
    }

    fun limpiar() = limpiarCache()
}
