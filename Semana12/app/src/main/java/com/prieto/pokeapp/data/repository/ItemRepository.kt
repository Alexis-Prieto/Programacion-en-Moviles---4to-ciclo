package com.prieto.pokeapp.domain.repository

import com.prieto.pokeapp.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun obtenerItems(): Flow<List<Item>>
    fun obtenerItemPorId(id: Int): Flow<Item?>
    suspend fun cambiarEstadoFavorito(id: Int, esFavorito: Boolean)
}