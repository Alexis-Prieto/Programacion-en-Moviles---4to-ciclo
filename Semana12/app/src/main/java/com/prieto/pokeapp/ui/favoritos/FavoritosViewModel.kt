package com.prieto.pokeapp.ui.favoritos

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

class FavoritosViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    val uiState: StateFlow<UiState<List<Item>>> = repository.favoritos
        .map<List<Item>, UiState<List<Item>>> { items ->
            UiState.Exito(items)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Cargando
        )

    fun removerDeFavoritos(id: Int) {
        viewModelScope.launch {
            repository.alternarFavorito(id)
        }
    }
}
