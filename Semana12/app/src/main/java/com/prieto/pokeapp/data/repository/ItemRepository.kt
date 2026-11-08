package com.prieto.pokeapp.data.repository

import com.prieto.pokeapp.domain.model.DetalleItem
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
    suspend fun obtenerDetalle(nombreOId: String): Result<DetalleItem>
}

class ErrorServidorException(val codigo: Int) : Exception()
