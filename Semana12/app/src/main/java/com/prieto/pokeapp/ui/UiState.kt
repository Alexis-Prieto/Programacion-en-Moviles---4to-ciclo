package com.prieto.pokeapp.ui

sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}