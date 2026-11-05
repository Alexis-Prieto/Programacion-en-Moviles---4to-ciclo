package com.prieto.pokeapp.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.domain.repository.ItemRepository
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ListaViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    init {
        cargarListaPokemon()
    }

    fun cargarListaPokemon() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            itemRepository.obtenerItems()
                .catch { e ->
                    _uiState.value = UiState.Error(e.localizedMessage ?: "Error al cargar Pokémon")
                }
                .collect { lista ->
                    _uiState.value = UiState.Exito(lista)
                }
        }
    }

    class Factory(private val repository: ItemRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListaViewModel::class.java)) {
                return ListaViewModel(repository) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}