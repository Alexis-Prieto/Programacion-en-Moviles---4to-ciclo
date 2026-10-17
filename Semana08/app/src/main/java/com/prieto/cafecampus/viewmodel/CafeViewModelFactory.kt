package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository

class CafeViewModelFactory(
    private val repository: CafeRepository,
    private val store: PreferenciasStore,
    private val clienteId: Int = 1
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SesionViewModel::class.java) -> {
                SesionViewModel(repository, store) as T
            }
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                MenuViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PedidosClienteViewModel::class.java) -> {
                PedidosClienteViewModel(repository, clienteId) as T
            }
            modelClass.isAssignableFrom(PanelAdminViewModel::class.java) -> {
                PanelAdminViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PedidosAdminViewModel::class.java) -> {
                PedidosAdminViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MenuAdminViewModel::class.java) -> {
                MenuAdminViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("ViewModel no reconocido: ${modelClass.name}")
        }
    }
}