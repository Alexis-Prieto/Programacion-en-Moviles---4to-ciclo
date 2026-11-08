package com.prieto.pokeapp.data.local

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val urlImagen: String,
    val esFavorito: Boolean = false,
    @ColumnInfo(defaultValue = "0") val enCatalogo: Boolean = false
)