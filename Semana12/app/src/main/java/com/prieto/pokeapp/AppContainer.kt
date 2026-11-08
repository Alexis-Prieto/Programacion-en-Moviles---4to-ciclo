package com.prieto.pokeapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prieto.pokeapp.data.auth.AuthRepository
import com.prieto.pokeapp.data.auth.FirebaseAuthRepository
import com.prieto.pokeapp.data.local.AppDatabase
import com.prieto.pokeapp.data.remote.RetrofitClient
import com.prieto.pokeapp.data.repository.ItemRepository
import com.prieto.pokeapp.data.repository.ItemRepositoryImpl
import com.prieto.pokeapp.notification.Notificador
import com.prieto.pokeapp.notification.NotificadorAndroid
import com.prieto.pokeapp.ui.admin.AdminViewModel
import com.prieto.pokeapp.ui.detalle.DetalleViewModel
import com.prieto.pokeapp.ui.favoritos.FavoritosViewModel
import com.prieto.pokeapp.ui.lista.ListaViewModel
import com.prieto.pokeapp.ui.login.LoginViewModel

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

class AppViewModelFactory(
    private val container: AppContainer,
    private val pokemonId: Int? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val instance = when (modelClass) {
            LoginViewModel::class.java -> LoginViewModel(container.authRepository)
            ListaViewModel::class.java -> ListaViewModel(container.itemRepository, container.notificador)
            AdminViewModel::class.java -> AdminViewModel(container.itemRepository, container.notificador)
            FavoritosViewModel::class.java -> FavoritosViewModel(container.itemRepository)
            DetalleViewModel::class.java -> DetalleViewModel(container.itemRepository, requireNotNull(pokemonId))
            else -> throw IllegalArgumentException("Clase ViewModel desconocida")
        }
        return instance as T
    }
}
