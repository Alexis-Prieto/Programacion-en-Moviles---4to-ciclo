package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository
import com.prieto.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MenuViewModel(
    private val repo: CafeRepository,
    private val prefsStore: PreferenciasStore
) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val nombreUsuario: StateFlow<String> = prefsStore.nombreUsuario
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    fun agregar(producto: Producto, cantidad: Int, nota: String = "") {
        _carrito.update { listaActual ->
            val existe = listaActual.find { it.producto.id == producto.id }
            if (existe != null) {
                listaActual.map { item ->
                    if (item.producto.id == producto.id) {
                        val notaFinal = if (nota.isNotBlank()) nota else item.nota
                        item.copy(
                            cantidad = item.cantidad + cantidad,
                            nota = notaFinal
                        )
                    } else {
                        item
                    }
                }
            } else {
                listaActual + ItemCarrito(producto = producto, cantidad = cantidad, nota = nota)
            }
        }
    }
    fun quitar(productoId: Int) {
        _carrito.update { lista ->
            lista.filterNot { it.producto.id == productoId }
        }
    }
    fun vaciar() {
        _carrito.value = emptyList()
    }
    fun total(): Double {
        return _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }
    fun obtenerProductoPorId(id: Int): Producto? {
        return productos.value.find { it.id == id }
    }
}