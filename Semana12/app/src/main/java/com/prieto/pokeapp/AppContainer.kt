package com.prieto.pokeapp

import com.prieto.pokeapp.data.remote.RetrofitClient
import com.prieto.pokeapp.data.repository.ItemRepositoryImpl
import com.prieto.pokeapp.domain.repository.ItemRepository

class AppContainer {
    val itemRepository: ItemRepository by lazy {
        ItemRepositoryImpl(apiService = RetrofitClient.apiService)
    }
}