package com.prieto.pokeapp.domain.model

data class Item(
    val id: Int,
    val nombre: String,
    val urlImagen: String = "",
    val esFavorito: Boolean = false
)