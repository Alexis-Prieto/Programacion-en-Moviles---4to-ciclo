package com.prieto.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MenuAdminViewModel(
    private val repository: CafeRepository)
    : ViewModel() {
    private val _categoriaSeleccionada = MutableStateFlow("Todo")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()
    val productosFiltrados = combine(
        repository.observarProductos(),
        _categoriaSeleccionada
    ) { lista, categoria ->
        if (categoria == "Todo") {
            lista
        } else {
            lista.filter { it.categoria.equals(categoria, ignoreCase = true) }
        }
    }
    fun cambiarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }
    fun guardarProducto(
        id: Int = 0,
        nombre: String,
        categoria: String,
        precio: Double,
        descripcion: String,
        disponible: Boolean,
        icono: String = "fastfood",
        onExito: () -> Unit
    ) {
        viewModelScope.launch {
            val producto = Producto(
                id = id,
                nombre = nombre.trim(),
                descripcion = descripcion.trim(),
                categoria = categoria.trim(),
                precio = precio,
                disponible = disponible,
                icono = icono
            )
            repository.guardarProducto(producto)
            onExito()
        }
    }
    fun alternarDisponibilidad(producto: Producto) {
        viewModelScope.launch {
            repository.cambiarDisponibilidad(producto.id, !producto.disponible)
        }
    }
    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.eliminarProducto(producto)
        }
    }
    suspend fun obtenerProductoPorId(id: Int): Producto? {
        return repository.observarProductos().first().find { it.id == id }
    }
}