package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository
import com.prieto.cafecampus.model.ItemCarrito
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
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

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow("Todo")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    val productosFiltrados: StateFlow<List<Producto>> = combine(
        repo.observarProductos(),
        _busqueda,
        _categoriaSeleccionada
    ) { lista, query, cat ->
        lista.filter { p ->
            val coincideBusqueda = p.nombre.contains(query, ignoreCase = true)
            val coincideCategoria = if (cat.equals("Todo", ignoreCase = true)) true
            else p.categoria.equals(cat, ignoreCase = true)
            coincideBusqueda && coincideCategoria
        }
    }.stateIn(
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

    val usuarioId: StateFlow<Int> = prefsStore.usuarioId
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    val carrito: StateFlow<List<ItemCarrito>> = prefsStore.usuarioId
        .flatMapLatest { id -> repo.observarCarrito(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onBusquedaChange(nuevaBusqueda: String) {
        _busqueda.value = nuevaBusqueda
    }
    fun onCategoriaChange(nuevaCategoria: String) {
        _categoriaSeleccionada.value = nuevaCategoria
    }
    fun agregar(producto: Producto, cantidad: Int = 1, nota: String = "") {
        viewModelScope.launch {
            val id = prefsStore.usuarioId.first()
            if (id != 0) {
                repo.agregarAlCarrito(id, producto, cantidad, nota)
            }
        }
    }
    fun quitar(productoId: Int) {
        viewModelScope.launch {
            val id = prefsStore.usuarioId.first()
            if (id != 0) {
                repo.quitarDelCarrito(id, productoId)
            }
        }
    }
    fun vaciar() {
        viewModelScope.launch {
            val id = prefsStore.usuarioId.first()
            if (id != 0) {
                repo.vaciarCarrito(id)
            }
        }
    }
    fun total(): Double {
        return carrito.value.sumOf { it.producto.precio * it.cantidad }
    }
    fun obtenerProductoPorId(id: Int): Producto? {
        return productos.value.find { it.id == id }
    }
}