package com.prieto.centrotareas.model

import kotlin.math.abs

data class ComparacionTiemposUi(
    val unaPorUnaMs: Long? = null,
    val aLaVezMs: Long? = null
) {
    val completa: Boolean
        get() = unaPorUnaMs != null && aLaVezMs != null

    val diferenciaMs: Long?
        get() {
            val secuencial = unaPorUnaMs ?: return null
            val paralelo = aLaVezMs ?: return null
            return abs(secuencial - paralelo)
        }

    val modalidadMasRapida: String?
        get() {
            val secuencial = unaPorUnaMs ?: return null
            val paralelo = aLaVezMs ?: return null
            return when {
                secuencial < paralelo -> "Una por una"
                paralelo < secuencial -> "A la vez"
                else -> "Empate"
            }
        }
}

sealed interface PrimosUi {
    data object Inicial : PrimosUi
    data object Calculando : PrimosUi
    data class Listo(
        val cantidad: Int,
        val milisegundos: Long
    ) : PrimosUi
    data object Cancelado : PrimosUi
}

sealed interface CargaUi {
    data object Inicial : CargaUi
    data object Cargando : CargaUi
    data class Exito(
        val modo: String,
        val perfil: String,
        val pedidos: Int,
        val milisegundos: Long
    ) : CargaUi
    data class Error(val mensaje: String) : CargaUi
}