package com.prieto.pokeapp.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.domain.repository.ItemRepository
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    val uiState: StateFlow<UiState<List<Item>>> = repository.obtenerItems()
        .map<List<Item>, UiState<List<Item>>> { items ->
            if (items.isEmpty()) UiState.Cargando else UiState.Exito(items)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Cargando
        )

    fun cambiarFavorito(id: Int, esFavorito: Boolean) {
        viewModelScope.launch {
            repository.cambiarEstadoFavorito(id, esFavorito)
        }
    }
}