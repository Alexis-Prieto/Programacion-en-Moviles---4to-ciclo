package com.prieto.pokeapp.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.domain.repository.ItemRepository
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<String>?>(null)
    val uiState: StateFlow<UiState<String>?> = _uiState.asStateFlow()

    fun limpiarCache() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            try {
                itemRepository.limpiarCache()
                _uiState.value = UiState.Exito("Caché limpiada correctamente")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Error al limpiar caché")
            }
        }
    }
}
