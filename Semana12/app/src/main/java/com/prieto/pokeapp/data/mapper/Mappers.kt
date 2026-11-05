package com.prieto.pokeapp.data.mapper

import com.prieto.pokeapp.data.remote.dto.ItemDto
import com.prieto.pokeapp.domain.model.Item

fun ItemDto.toDomain(): Item {
    val id = this.idExtraido
    val urlImagen = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    return Item(
        id = id,
        nombre = this.nombre,
        urlImagen = urlImagen,
        esFavorito = false
    )
}