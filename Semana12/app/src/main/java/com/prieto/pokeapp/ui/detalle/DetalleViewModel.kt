package com.prieto.pokeapp.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.data.remote.dto.ItemDetailDto
import com.prieto.pokeapp.domain.repository.ItemRepository
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val itemRepository: ItemRepository,
    private val pokemonId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ItemDetailDto>>(UiState.Cargando)
    val uiState: StateFlow<UiState<ItemDetailDto>> = _uiState.asStateFlow()

    init {
        cargarDetalle()
    }

    fun cargarDetalle() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            itemRepository.obtenerDetalle(pokemonId)
                .onSuccess { detalle ->
                    _uiState.value = UiState.Exito(detalle)
                }
                .onFailure { e ->
                    _uiState.value = UiState.Error(e.localizedMessage ?: "Error al cargar detalle")
                }
        }
    }

    class Factory(
        private val repository: ItemRepository,
        private val pokemonId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetalleViewModel::class.java)) {
                return DetalleViewModel(repository, pokemonId) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}
