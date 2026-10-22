package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.data.repository.CafeRepository
import com.prieto.cafecampus.model.EstadoPedido
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PedidosAdminViewModel(private val repository: CafeRepository) : ViewModel() {

    val estadoSeleccionado = MutableStateFlow("RECIBIDO")

    val pedidos: StateFlow<List<PedidoConDetalle>> = estadoSeleccionado.flatMapLatest { estadoStr ->
        if (estadoStr.uppercase() == "HISTORIAL") {
            combine(
                EstadoPedido.values().map { estadoEnum ->
                    repository.observarPedidosPorEstado(estadoEnum)
                }
            ) { listasDePedidos ->
                listasDePedidos.flatMap { it }.sortedByDescending { it.pedido.id }
            }
        } else {
            val estadoEnum = try {
                EstadoPedido.valueOf(estadoStr.uppercase())
            } catch (e: Exception) {
                EstadoPedido.RECIBIDO
            }
            repository.observarPedidosPorEstado(estadoEnum)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Mapa de clienteId -> Nombre real del Usuario
    val nombresClientes: StateFlow<Map<Int, String>> = pedidos.flatMapLatest { pedidosList ->
        val clienteIds = pedidosList.map { it.pedido.clienteId }.distinct()
        if (clienteIds.isEmpty()) {
            flowOf(emptyMap())
        } else {
            val usuarioFlows = clienteIds.map { id ->
                repository.observarUsuarioPorId(id)
            }
            combine(usuarioFlows) { usuarios ->
                usuarios.filterNotNull().associate { u ->
                    u.id to u.nombre.ifBlank { "Cliente #${u.id}" }
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    fun cambiarFiltro(nuevoEstado: String) {
        estadoSeleccionado.value = nuevoEstado
    }

    fun obtenerSiguienteEstado(estadoActualStr: String): EstadoPedido? {
        val estado = try {
            EstadoPedido.valueOf(estadoActualStr.uppercase())
        } catch (e: Exception) {
            null
        }
        return when (estado) {
            EstadoPedido.RECIBIDO -> EstadoPedido.PREPARANDO
            EstadoPedido.PREPARANDO -> EstadoPedido.LISTO
            EstadoPedido.LISTO -> EstadoPedido.ENTREGADO
            else -> null
        }
    }

    fun obtenerTextoBotonAvanzar(estadoActualStr: String): String {
        val estado = try {
            EstadoPedido.valueOf(estadoActualStr.uppercase())
        } catch (e: Exception) {
            null
        }
        return when (estado) {
            EstadoPedido.RECIBIDO -> "Pasar a Preparando"
            EstadoPedido.PREPARANDO -> "Pasar a Listo"
            EstadoPedido.LISTO -> "Marcar Entregado"
            else -> "Pasar a..."
        }
    }

    fun avanzar(pedidoConDetalle: PedidoConDetalle) {
        val estadoActualStr = pedidoConDetalle.pedido.estado.toString()
        val siguiente = obtenerSiguienteEstado(estadoActualStr) ?: return
        viewModelScope.launch {
            repository.cambiarEstado(pedidoConDetalle.pedido.id, siguiente)
        }
    }

    fun cambiarEstadoPorId(pedidoId: Int, nuevoEstadoStr: String) {
        val estadoEnum = try {
            EstadoPedido.valueOf(nuevoEstadoStr.uppercase())
        } catch (e: Exception) {
            return
        }
        viewModelScope.launch {
            repository.cambiarEstado(pedidoId, estadoEnum)
        }
    }

    fun cancelar(pedidoConDetalle: PedidoConDetalle) {
        viewModelScope.launch {
            repository.cambiarEstado(pedidoConDetalle.pedido.id, EstadoPedido.CANCELADO)
        }
    }

    fun cancelarPorId(pedidoId: Int) {
        viewModelScope.launch {
            repository.cambiarEstado(pedidoId, EstadoPedido.CANCELADO)
        }
    }
}