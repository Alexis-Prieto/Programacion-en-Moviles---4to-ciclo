package com.prieto.cafecampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.prieto.cafecampus.data.local.entity.DetallePedido
import com.prieto.cafecampus.data.local.entity.Pedido
import com.prieto.cafecampus.data.local.entity.Usuario

data class PedidoConDetalle(
    @Embedded val pedido: Pedido,

    @Relation(
        entity = Usuario::class,
        parentColumn = "clienteId",
        entityColumn = "id"
    )
    val cliente: Usuario,

    @Relation(
        entity = DetallePedido::class,
        parentColumn = "id",
        entityColumn = "pedidoId"
    )
    val detalles: List<DetallePedido>
)