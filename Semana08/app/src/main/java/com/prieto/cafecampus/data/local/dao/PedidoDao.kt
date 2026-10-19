package com.prieto.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.prieto.cafecampus.data.local.entity.DetallePedido
import com.prieto.cafecampus.data.local.entity.Pedido
import com.prieto.cafecampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPedido(p: Pedido): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDetalles(lista: List<DetallePedido>)

    @Query("UPDATE pedidos SET estado = :estado WHERE id = :id")
    suspend fun cambiarEstado(id: Int, estado: String)

    @Transaction
    suspend fun crearPedido(pedido: Pedido, detalles: List<DetallePedido>): Long {
        val pedidoId = insertarPedido(pedido).toInt()
        val detallesConId = detalles.map { it.copy(pedidoId = pedidoId) }
        insertarDetalles(detallesConId)
        return pedidoId.toLong()
    }
    @Transaction
    @Query("SELECT * FROM pedidos WHERE clienteId = :clienteId ORDER BY fecha DESC")
    fun observarPorCliente(clienteId: Int): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE estado = :estado ORDER BY fecha DESC")
    fun observarPorEstado(estado: String): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :id LIMIT 1")
    fun observarUno(id: Int): Flow<PedidoConDetalle?>

    @Transaction
    @Query("SELECT * FROM pedidos ORDER BY fecha DESC")
    fun observarTodos(): Flow<List<PedidoConDetalle>>

    @Query("SELECT COALESCE(SUM(total), 0.0) FROM pedidos WHERE fecha >= :inicio AND estado != 'CANCELADO'")
    fun ventasDesde(inicio: Long): Flow<Double>

    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    fun cantidadPorEstado(estado: String): Flow<Int>
}