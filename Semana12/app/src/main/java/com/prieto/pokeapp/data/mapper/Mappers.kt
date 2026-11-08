package com.prieto.pokeapp.data.mapper

import com.prieto.pokeapp.data.local.ItemEntity
import com.prieto.pokeapp.data.remote.dto.ItemDto
import com.prieto.pokeapp.data.remote.dto.ItemDetailDto
import com.prieto.pokeapp.domain.model.DetalleItem
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.domain.model.HabilidadPokemon
import com.prieto.pokeapp.domain.model.EstadisticaPokemon

fun ItemDetailDto.toDomain(): DetalleItem = DetalleItem(
    id = id, nombre = nombre, altura = altura, peso = peso, urlImagen = sprites?.urlImagen,
    experienciaBase = experienciaBase,
    tipos = tipos.map { it.tipo.nombre },
    habilidades = habilidades.map { HabilidadPokemon(it.habilidad.nombre, it.oculta) },
    estadisticas = estadisticas.map { EstadisticaPokemon(it.estadistica.nombre, it.valor) },
    movimientos = movimientos.map { it.movimiento.nombre }
)

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