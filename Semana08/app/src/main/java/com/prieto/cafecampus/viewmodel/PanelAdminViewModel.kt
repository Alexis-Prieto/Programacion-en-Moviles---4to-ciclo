package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.local.dao.PedidosPorHora
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.data.repository.CafeRepository
import com.prieto.cafecampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class PanelAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val inicioHoy: Long
        get() {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }

    val nuevos: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoPedido.RECIBIDO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val preparando: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoPedido.PREPARANDO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val listos: StateFlow<Int> = repository.observarCantidadPorEstado(EstadoPedido.LISTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val ventasHoy: StateFlow<Double> = repository.observarVentasHoy(inicioHoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalPedidosHoy: StateFlow<Int> = repository.observarCantidadPedidosDesde(inicioHoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val nuevosPedidosLista: StateFlow<List<PedidoConDetalle>> = repository.observarPedidosPorEstado(EstadoPedido.RECIBIDO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pedidosPorHora: StateFlow<List<PedidosPorHora>> = repository.getPedidosPorHora()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun prepararPedido(pedidoId: Int) {
        viewModelScope.launch {
            repository.cambiarEstado(pedidoId, EstadoPedido.PREPARANDO)
        }
    }
}