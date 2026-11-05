package com.prieto.pokeapp.ui.lista

import androidx.lifecycle.ViewModel
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
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    init {
        cargarListaPokemon()
    }
    fun cargarListaPokemon() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            repository.obtenerItems()
                .catch { _ ->
                    _uiState.value = UiState.Error("Sin conexión a Internet.")
                }
                .collect { items ->
                    if (items.isEmpty()) {
                        _uiState.value = UiState.Error("Sin conexión a Internet y no hay datos locales en Room.")
                    } else {
                        _uiState.value = UiState.Exito(items)
                    }
                }
        }
    }
    fun cambiarFavorito(id: Int, esFavorito: Boolean) {
        viewModelScope.launch {
            repository.cambiarEstadoFavorito(id, esFavorito)
        }
    }
}