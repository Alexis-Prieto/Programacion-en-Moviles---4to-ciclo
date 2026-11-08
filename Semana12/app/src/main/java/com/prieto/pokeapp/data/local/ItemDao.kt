package com.prieto.pokeapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM items WHERE enCatalogo = 1 ORDER BY id")
    fun todos(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE esFavorito = 1 ORDER BY id")
    fun favoritos(): Flow<List<ItemEntity>>

    @Query("SELECT id FROM items WHERE esFavorito = 1")
    suspend fun idsFavoritos(): List<Int>

    @Query("SELECT COUNT(*) FROM items WHERE enCatalogo = 1")
    suspend fun contar(): Int

    @Query("SELECT COUNT(*) FROM items WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int

    @Upsert
    suspend fun guardarTodos(items: List<ItemEntity>)

    @Query("UPDATE items SET enCatalogo = 0 WHERE enCatalogo = 1")
    suspend fun desactivarCatalogo()

    @Transaction
    suspend fun actualizarCatalogo(items: List<ItemEntity>) {
        val favoritos = idsFavoritos().toSet()
        desactivarCatalogo()
        guardarTodos(items.map { it.copy(esFavorito = it.id in favoritos, enCatalogo = true) })
    }

    @Query("UPDATE items SET esFavorito = NOT esFavorito WHERE id = :id")
    suspend fun alternarFavorito(id: Int)

    @Query("DELETE FROM items")
    suspend fun borrarTodo()
}