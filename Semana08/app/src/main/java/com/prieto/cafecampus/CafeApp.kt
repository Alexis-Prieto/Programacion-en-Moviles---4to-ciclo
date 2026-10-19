package com.prieto.cafecampus

import android.app.Application
import com.prieto.cafecampus.data.local.CafeDatabase
import com.prieto.cafecampus.data.local.DatosIniciales
import com.prieto.cafecampus.data.repository.CafeRepository
import com.prieto.cafecampus.data.repository.CafeRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CafeApp : Application() {

    val database by lazy { CafeDatabase.obtener(this) }

    val repository: CafeRepository by lazy {
        CafeRepositoryRoom(
            usuarioDao = database.usuarioDao(),
            productoDao = database.productoDao(),
            pedidoDao = database.pedidoDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }
}