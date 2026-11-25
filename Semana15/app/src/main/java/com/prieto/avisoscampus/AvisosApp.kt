package com.prieto.avisoscampus

import android.app.Application
import com.prieto.avisoscampus.notificacion.Notificador

class AvisosApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Notificador.crearCanal(this)
    }
}