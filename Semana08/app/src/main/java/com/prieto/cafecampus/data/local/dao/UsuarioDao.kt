package com.prieto.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prieto.cafecampus.data.local.entity.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(u: Usuario): Long

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuario?

    @Query("SELECT COUNT(*) FROM usuarios WHERE LOWER(correo) = LOWER(:correo)")
    suspend fun existeCorreo(correo: String): Int

    @Query("SELECT * FROM usuarios WHERE LOWER(correo) = LOWER(:correo) LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): Usuario?
}