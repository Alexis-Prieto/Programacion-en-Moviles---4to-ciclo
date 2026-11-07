package com.prieto.pokeapp.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.data.repository.ItemRepository
import com.prieto.pokeapp.notification.Notificador
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AdminViewModel(
    private val itemRepository: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<String>?>(null)
    val uiState: StateFlow<UiState<String>?> = _uiState.asStateFlow()

    private val _totalGuardados = MutableStateFlow(0)
    val totalGuardados: StateFlow<Int> = _totalGuardados.asStateFlow()

    private val _totalFavoritos = MutableStateFlow(0)
    val totalFavoritos: StateFlow<Int> = _totalFavoritos.asStateFlow()

    init {
        actualizarTotales()
    }

    fun actualizarTotales() {
        viewModelScope.launch {
            _totalGuardados.value = itemRepository.cantidadGuardada()
            _totalFavoritos.value = itemRepository.cantidadFavoritos()
        }
    }

    fun sincronizarDatos() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            val resultado = itemRepository.refrescar()
            resultado
                .onSuccess { n ->
                    actualizarTotales()
                    notificador.mostrarNotificacion("PokeApp", "Se guardaron $n elementos")
                    _uiState.value = UiState.Exito("Sincronización completada ($n elementos)")
                }
                .onFailure { e ->
                    val mensaje = when (e) {
                        is IOException -> "No hay conexión a internet."
                        is HttpException -> if (e.code() == 404) "No se encontró la información del Pokémon." else "El servidor no responde. Intenta más tarde."
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
                actualizarTotales()
                _uiState.value = UiState.Exito("Caché limpiada correctamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Ocurrió un error inesperado.")
            }
        }
    }

    fun limpiar() = limpiarCache()
}
