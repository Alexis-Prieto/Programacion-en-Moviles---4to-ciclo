package com.prieto.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prieto.cafecampus.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY nombre")
    fun observarTodos(): Flow<List<Producto>>

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(p: Producto): Long

    @Update
    suspend fun actualizar(p: Producto)
    @Delete
    suspend fun eliminar(p: Producto)

    @Query("UPDATE productos SET disponible = :d WHERE id = :id")
    suspend fun cambiarDisponibilidad(id: Int, d: Boolean)

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Producto?

    @Query("SELECT * FROM productos WHERE categoria = :categoria ORDER BY nombre")
    fun observarPorCategoria(categoria: String): Flow<List<Producto>>
}