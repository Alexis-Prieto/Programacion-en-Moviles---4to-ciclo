package com.prieto.cafecampus.data.repository

import com.prieto.cafecampus.data.local.dao.PedidoDao
import com.prieto.cafecampus.data.local.dao.PedidosPorHora
import com.prieto.cafecampus.data.local.dao.ProductoDao
import com.prieto.cafecampus.data.local.dao.UsuarioDao
import com.prieto.cafecampus.data.local.entity.DetallePedido
import com.prieto.cafecampus.data.local.entity.Pedido
import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.data.local.entity.Usuario
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import com.prieto.cafecampus.model.EstadoPedido
import com.prieto.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

class CafeRepositoryRoom(
    private val usuarioDao: UsuarioDao,
    private val productoDao: ProductoDao,
    private val pedidoDao: PedidoDao
) : CafeRepository {

    override suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo = correo, clave = clave)
    }

    override fun observarProductos(): Flow<List<Producto>> {
        return productoDao.observarTodos()
    }

    override suspend fun guardarProducto(p: Producto) {
        if (p.id == 0) {
            productoDao.insertar(p)
        } else {
            productoDao.actualizar(p)
        }
    }
    override suspend fun eliminarProducto(p: Producto) {
        productoDao.eliminar(p)
    }
    override suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean) {
        productoDao.cambiarDisponibilidad(id, disponible)
    }

    override suspend fun crearPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ): Long {
        if (items.isEmpty()) return -1L

        val total = items.sumOf { item -> item.producto.precio * item.cantidad }

        val nuevoPedido = Pedido(
            clienteId = clienteId,
            estado = EstadoPedido.RECIBIDO.name,
            horaRecojo = horaRecojo,
            pago = pago,
            nota = nota,
            total = total,
            fecha = System.currentTimeMillis()
        )
        val detalles = items.map { item ->
            DetallePedido(
                pedidoId = 0,
                productoId = item.producto.id,
                cantidad = item.cantidad,
                precioUnitario = item.producto.precio,
                nombreProducto = item.producto.nombre
            )
        }
        return pedidoDao.crearPedido(nuevoPedido, detalles)
    }

    override fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorCliente(clienteId)
    }

    override fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>> {
        return pedidoDao.observarPorEstado(estado.name)
    }

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> {
        return pedidoDao.observarUno(id)
    }

    override suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido) {
        pedidoDao.cambiarEstado(pedidoId, estado.name)
    }

    override fun observarVentasHoy(inicioDia: Long): Flow<Double> {
        return pedidoDao.ventasDesde(inicioDia)
    }

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> {
        return pedidoDao.cantidadPorEstado(estado.name)
    }

    override fun observarCantidadPedidosDesde(inicioDia: Long): Flow<Int> {
        return pedidoDao.cantidadPedidosDesde(inicioDia)
    }

    override fun getPedidosPorHora(): Flow<List<PedidosPorHora>> {
        return pedidoDao.getPedidosPorHora()
    }
}