package com.prieto.pokeapp

import android.content.Context
import com.prieto.pokeapp.data.auth.AuthRepository
import com.prieto.pokeapp.data.auth.FirebaseAuthRepository
import com.prieto.pokeapp.data.local.AppDatabase
import com.prieto.pokeapp.data.remote.RetrofitClient
import com.prieto.pokeapp.data.repository.ItemRepositoryImpl
import com.prieto.pokeapp.domain.repository.ItemRepository
import com.prieto.pokeapp.notification.Notificador
import com.prieto.pokeapp.notification.NotificadorAndroid

class AppContainer(context: Context) {
    private val database = AppDatabase.obtenerBaseDatos(context)

    val notificador: Notificador by lazy {
        NotificadorAndroid(context)
    }
    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(
            apiService = RetrofitClient.apiService,
            itemDao = database.itemDao()
        )
    }
    val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository()
    }
}