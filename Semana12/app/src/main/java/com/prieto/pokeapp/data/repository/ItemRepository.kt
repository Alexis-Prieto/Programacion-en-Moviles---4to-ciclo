package com.prieto.pokeapp.domain.repository

import com.prieto.pokeapp.data.remote.dto.ItemDetailDto
import com.prieto.pokeapp.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun obtenerItems(): Flow<List<Item>>
    fun obtenerDetallePokemon(id: Int): Flow<ItemDetailDto>
    suspend fun cambiarEstadoFavorito(id: Int, esFavorito: Boolean)
}