package com.prieto.pokeapp.data.mapper

import com.prieto.pokeapp.data.local.ItemEntity
import com.prieto.pokeapp.data.remote.dto.ItemDto
import com.prieto.pokeapp.domain.model.Item

fun ItemDto.toEntity(): ItemEntity {
    val id = this.idExtraido
    val urlImagen = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    return ItemEntity(
        id = id,
        nombre = this.nombre,
        urlImagen = urlImagen,
        esFavorito = false
    )
}
fun ItemEntity.toDomain(): Item {
    return Item(
        id = id,
        nombre = nombre,
        urlImagen = urlImagen,
        esFavorito = esFavorito
    )
}
fun Item.toEntity(): ItemEntity {
    return ItemEntity(
        id = id,
        nombre = nombre,
        urlImagen = urlImagen,
        esFavorito = esFavorito
    )
}
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