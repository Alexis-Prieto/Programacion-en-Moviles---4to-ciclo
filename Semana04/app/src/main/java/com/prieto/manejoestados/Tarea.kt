package com.prieto.manejoestados

data class Tarea(
    val id: Int,
    val descripcion: String,
    val completada: Boolean = false
)