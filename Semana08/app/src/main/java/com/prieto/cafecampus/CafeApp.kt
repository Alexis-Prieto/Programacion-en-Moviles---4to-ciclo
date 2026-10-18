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

    lateinit var repository: CafeRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val db = CafeDatabase.obtener(this)
        repository = CafeRepositoryRoom(db)

        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(db)
        }
    }
}