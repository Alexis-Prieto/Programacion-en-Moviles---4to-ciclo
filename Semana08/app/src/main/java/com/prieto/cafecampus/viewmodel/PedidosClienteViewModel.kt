package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.data.repository.CafeRepository
import com.prieto.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosClienteViewModel(
    private val repository: CafeRepository,
    private val usuarioId: Int)
    : ViewModel() {
    val misPedidos: StateFlow<List<PedidoConDetalle>> = repository.observarPedidosCliente(usuarioId)
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
            val pedidoId = repository.crearPedido(
                clienteId = usuarioId,
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
        private val usuarioId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PedidosClienteViewModel::class.java)) {
                return PedidosClienteViewModel(repository, usuarioId) as T
            }
            throw IllegalArgumentException("ViewModel desconocido")
        }
    }
}