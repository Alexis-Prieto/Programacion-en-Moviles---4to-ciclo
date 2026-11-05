package com.prieto.pokeapp

import android.content.Context
import com.prieto.pokeapp.data.local.AppDatabase
import com.prieto.pokeapp.data.remote.RetrofitClient
import com.prieto.pokeapp.data.repository.ItemRepositoryImpl
import com.prieto.pokeapp.domain.repository.ItemRepository

class AppContainer(context: Context) {
    private val database = AppDatabase.obtenerBaseDatos(context)

    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(
            apiService = RetrofitClient.apiService,
            itemDao = database.itemDao()
        )
    }
}