package com.prieto.avisoscampus.notificacion

import android.content.Context
import com.prieto.avisoscampus.model.Aviso

object Notificador {
    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        // TODO Paso 1
    }
    fun tienePermiso(context: Context): Boolean {
        // TODO Paso 2
        return false
    }
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO Paso 3
        return false
    }
}