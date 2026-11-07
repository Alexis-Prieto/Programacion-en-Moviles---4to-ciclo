package com.prieto.pokeapp.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.data.remote.dto.ItemDetailDto
import com.prieto.pokeapp.data.repository.ItemRepository
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetalleViewModel(
    private val itemRepository: ItemRepository,
    val pokemonId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ItemDetailDto>>(UiState.Cargando)
    val uiState: StateFlow<UiState<ItemDetailDto>> = _uiState.asStateFlow()

    val esFavorito: StateFlow<Boolean> = itemRepository.favoritos
        .map { listaFav ->
            listaFav.any { it.id == pokemonId }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    init {
        cargarDetalle()
    }

    fun cargarDetalle() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            itemRepository.obtenerDetalle(pokemonId.toString())
                .onSuccess { detalle ->
                    _uiState.value = UiState.Exito(detalle)
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

    fun alternarFavorito() {
        viewModelScope.launch {
            itemRepository.alternarFavorito(pokemonId)
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
