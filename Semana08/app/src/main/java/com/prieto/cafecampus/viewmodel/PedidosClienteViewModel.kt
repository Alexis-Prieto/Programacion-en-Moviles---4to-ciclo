package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository
import com.prieto.cafecampus.model.ItemCarrito
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PedidosClienteViewModel(
    private val repository: CafeRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    val estaRecibiendoPedidos: StateFlow<Boolean> = preferenciasStore.recibirPedidos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val misPedidos: StateFlow<List<PedidoConDetalle>> = preferenciasStore.usuarioId
        .flatMapLatest { id ->
            if (id != 0) {
                repository.observarPedidosCliente(id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun pedido(id: Int): StateFlow<PedidoConDetalle?> {
        return repository.observarPedido(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }

    fun confirmar(
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String,
        onSuccess: (Int) -> Unit
    ) {
        if (items.isEmpty()) return
        viewModelScope.launch {
            val idActual = preferenciasStore.usuarioId.first()
            val pedidoId = repository.crearPedido(
                clienteId = idActual,
                items = items,
                horaRecojo = horaRecojo,
                pago = pago,
                nota = nota
            )
            onSuccess(pedidoId.toInt())
        }
    }

    class Factory(
        private val repository: CafeRepository,
        private val preferenciasStore: PreferenciasStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PedidosClienteViewModel::class.java)) {
                return PedidosClienteViewModel(repository, preferenciasStore) as T
            }
            throw IllegalArgumentException("ViewModel desconocido")
        }
    }
}