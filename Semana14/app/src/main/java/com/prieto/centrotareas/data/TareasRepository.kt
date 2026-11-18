package com.prieto.centrotareas.data

import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    fun ejecutarTareaConProgreso(): Flow<Int>

    suspend fun contarPrimos(
        hasta: Int,
        onProgreso: suspend (Int) -> Unit
    ): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}