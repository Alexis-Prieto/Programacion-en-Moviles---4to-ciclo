package com.prieto.pokeapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponseDto(
    @SerialName("results") val resultados: List<ItemDto>
)

@Serializable
data class ItemDto(
    @SerialName("name") val nombre: String,
    @SerialName("url") val url: String
) {
    val idExtraido: Int
        get() = url.trimEnd('/').split("/").lastOrNull()?.toIntOrNull() ?: 0
}

@Serializable
data class ItemDetailDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val nombre: String,
    @SerialName("height") val altura: Int = 0,
    @SerialName("weight") val peso: Int = 0,
    @SerialName("sprites") val sprites: SpritesDto? = null
)

@Serializable
data class SpritesDto(
    @SerialName("front_default") val urlImagen: String? = null
)