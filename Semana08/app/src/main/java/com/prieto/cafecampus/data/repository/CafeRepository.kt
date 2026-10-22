package com.prieto.cafecampus.data.repository

import com.prieto.cafecampus.data.local.dao.PedidosPorHora
import com.prieto.cafecampus.data.local.dao.ProductoMasVendido
import com.prieto.cafecampus.data.local.dao.TotalPorFormaPago
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.data.local.entity.Usuario
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.model.EstadoPedido
import com.prieto.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

interface CafeRepository {

    fun observarCarrito(usuarioId: Int): Flow<List<ItemCarrito>>
    suspend fun agregarAlCarrito(usuarioId: Int, producto: Producto, cantidad: Int, nota: String = "")
    suspend fun quitarDelCarrito(usuarioId: Int, productoId: Int)
    suspend fun vaciarCarrito(usuarioId: Int)

    suspend fun login(correo: String, clave: String): Usuario?
    fun observarProductos(): Flow<List<Producto>>
    suspend fun guardarProducto(p: Producto)
    suspend fun eliminarProducto(p: Producto)
    suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean)

    suspend fun crearPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ): Long
    fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>>
    fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>>
    fun observarPedido(id: Int): Flow<PedidoConDetalle?>
    suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido)

    fun observarVentasHoy(inicioDia: Long): Flow<Double>
    fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int>
    fun observarCantidadPedidosDesde(inicioDia: Long): Flow<Int>

    fun getPedidosPorHora(): Flow<List<PedidosPorHora>>

    fun getProductoMasVendidoDelDia(inicioDia: Long): Flow<ProductoMasVendido?>
    fun getTotalPorFormaPagoDelDia(inicioDia: Long): Flow<List<TotalPorFormaPago>>

    suspend fun existeCorreo(correo: String): Boolean
    suspend fun registrarEstudiante(nombre: String, correo: String, clave: String): Long

    fun observarUsuarioPorId(id: Int): Flow<Usuario?>
    suspend fun actualizarDatosUsuario(id: Int, nombre: String, correo: String)
}