package com.prieto.pokeapp.data.repository

import com.prieto.pokeapp.data.local.ItemDao
import com.prieto.pokeapp.data.mapper.toDomain
import com.prieto.pokeapp.data.mapper.toEntity
import com.prieto.pokeapp.data.remote.ApiService
import com.prieto.pokeapp.data.remote.dto.ItemDetailDto
import com.prieto.pokeapp.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    override val items: Flow<List<Item>> = itemDao.todos().map { lista ->
        lista.map { it.toDomain() }
    }

    override val favoritos: Flow<List<Item>> = itemDao.favoritos().map { lista ->
        lista.map { it.toDomain() }
    }

    override suspend fun refrescar(): Result<Int> = try {
        val respuesta = apiService.obtenerListaPokemon(limite = 100)
        val idsFavoritos = itemDao.idsFavoritos().toSet()
        val entidades = respuesta.resultados.map { dto ->
            val entity = dto.toEntity()
            entity.copy(esFavorito = idsFavoritos.contains(entity.id))
        }
        itemDao.guardarTodos(entidades)
        Result.success(entidades.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) = itemDao.alternarFavorito(id)

    override suspend fun cantidadGuardada(): Int = itemDao.contar()

    override suspend fun cantidadFavoritos(): Int = itemDao.contarFavoritos()

    override suspend fun limpiarCache() = itemDao.borrarTodo()

    override suspend fun obtenerDetalle(nombreOId: String): Result<ItemDetailDto> = try {
        Result.success(apiService.obtenerDetallePokemon(nombreOId))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
