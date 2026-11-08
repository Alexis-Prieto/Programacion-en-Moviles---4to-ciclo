package com.prieto.pokeapp.domain.model

data class Item(
    val id: Int,
    val nombre: String,
    val urlImagen: String = "",
    val esFavorito: Boolean = false
)

data class DetalleItem(
    val id: Int,
    val nombre: String,
    val altura: Int,
    val peso: Int,
    val urlImagen: String?,
    val experienciaBase: Int? = null,
    val tipos: List<String> = emptyList(),
    val habilidades: List<HabilidadPokemon> = emptyList(),
    val estadisticas: List<EstadisticaPokemon> = emptyList(),
    val movimientos: List<String> = emptyList()
)

data class HabilidadPokemon(val nombre: String, val oculta: Boolean)
data class EstadisticaPokemon(val nombre: String, val valor: Int)