package com.prieto.centrotareas.data

interface TareasRepository {
    suspend fun contarPrimos(
        hasta: Int,
        onProgreso: suspend (Int) -> Unit
    ): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}