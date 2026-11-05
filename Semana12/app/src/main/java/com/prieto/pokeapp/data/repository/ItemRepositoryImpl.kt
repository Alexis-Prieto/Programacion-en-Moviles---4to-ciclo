package com.prieto.pokeapp.data.repository

import com.prieto.pokeapp.data.local.ItemDao
import com.prieto.pokeapp.data.mapper.toDomain
import com.prieto.pokeapp.data.mapper.toEntity
import com.prieto.pokeapp.data.remote.ApiService
import com.prieto.pokeapp.data.remote.dto.ItemDetailDto
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    override fun obtenerItems(): Flow<List<Item>> {
        return itemDao.todos()
            .map { entidades -> entidades.map { it.toDomain() } }
            .onStart {
                try {
                    val respuesta = apiService.obtenerListaPokemon()
                    val idsFavoritos = itemDao.idsFavoritos().toSet()
                    val entidades = respuesta.resultados.map { dto ->
                        val entity = dto.toEntity()
                        entity.copy(esFavorito = idsFavoritos.contains(entity.id))
                    }
                    itemDao.guardarTodos(entidades)
                } catch (_: Exception) {
                }
            }
    }
    override fun obtenerDetallePokemon(id: Int): Flow<ItemDetailDto> = flow {
        val detalle = apiService.obtenerDetallePokemon(id)
        emit(detalle)
    }

    override suspend fun cambiarEstadoFavorito(id: Int, esFavorito: Boolean) {
        itemDao.alternarFavorito(id)
    }
}