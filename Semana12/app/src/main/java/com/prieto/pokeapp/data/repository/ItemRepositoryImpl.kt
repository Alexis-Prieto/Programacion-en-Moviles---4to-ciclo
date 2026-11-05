package com.prieto.pokeapp.data.repository

import com.prieto.pokeapp.data.mapper.toDomain
import com.prieto.pokeapp.data.remote.ApiService
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ItemRepositoryImpl(
    private val apiService: ApiService
) : ItemRepository {

    override fun obtenerItems(): Flow<List<Item>> = flow {
        val respuesta = apiService.obtenerListaPokemon(limite = 100)
        val listaDominio = respuesta.resultados.map { it.toDomain() }
        emit(listaDominio)
    }

    override fun obtenerItemPorId(id: Int): Flow<Item?> = flow {
        val detalle = apiService.obtenerDetallePokemon(id)
        val item = Item(
            id = detalle.id,
            nombre = detalle.nombre,
            urlImagen = detalle.sprites?.urlImagen ?: "",
            esFavorito = false
        )
        emit(item)
    }

    override suspend fun cambiarEstadoFavorito(id: Int, esFavorito: Boolean) {
    }
}