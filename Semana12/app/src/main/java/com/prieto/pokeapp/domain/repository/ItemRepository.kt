package com.prieto.pokeapp.domain.repository

import com.prieto.pokeapp.data.remote.dto.ItemDetailDto
import com.prieto.pokeapp.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    val items: Flow<List<Item>>
    val favoritos: Flow<List<Item>>
    suspend fun refrescar(): Result<Int>
    suspend fun alternarFavorito(id: Int)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
    suspend fun obtenerDetalle(nombreOId: String): Result<ItemDetailDto>
}
