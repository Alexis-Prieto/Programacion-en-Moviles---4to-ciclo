package com.prieto.pokeapp.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()

    init {
        cargarDatosDePrueba()
    }

    fun cargarDatosDePrueba() {
        viewModelScope.launch {
            _uiState.value = UiState.Cargando
            delay(1000) // Simula tiempo de carga

            val datosMock = listOf(
                Item(id = 1, nombre = "bulbasaur", urlImagen = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png", esFavorito = false),
                Item(id = 4, nombre = "charmander", urlImagen = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png", esFavorito = true),
                Item(id = 7, nombre = "squirtle", urlImagen = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png", esFavorito = false),
                Item(id = 25, nombre = "pikachu", urlImagen = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png", esFavorito = true)
            )

            _uiState.value = UiState.Exito(datosMock)
        }
    }
}