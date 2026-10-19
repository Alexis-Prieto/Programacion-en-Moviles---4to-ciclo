package com.prieto.cafecampus.ui.navigation

sealed class Ruta(val ruta: String) {
    object Splash : Ruta("splash")
    object Login : Ruta("login")

    object Registro : Ruta("registro")
    object ClienteHome : Ruta("cliente_home")
    object MisPedidos : Ruta("mis_pedidos")
    object Perfil : Ruta("perfil")
    object ConfirmarPedido : Ruta("confirmar_pedido")

    object DetalleProducto : Ruta("detalle_producto/{productoId}") {
        fun crearRuta(productoId: Int) = "detalle_producto/$productoId"
    }
    object Seguimiento : Ruta("seguimiento/{pedidoId}") {
        fun crearRuta(pedidoId: Int) = "seguimiento/$pedidoId"
    }

    object AdminHome : Ruta("admin_home")
    object AdminPedidos : Ruta("admin_pedidos")
    object AdminMenu : Ruta("admin_menu")
    object AdminAjustes : Ruta("admin_ajustes")

    object AdminFormularioProducto : Ruta("admin_formulario_producto/{productoId}") {
        fun crearRuta(productoId: Int) = "admin_formulario_producto/$productoId"
    }
    object AdminDetallePedido : Ruta("admin_detalle_pedido/{pedidoId}") {
        fun crearRuta(pedidoId: Int) = "admin_detalle_pedido/$pedidoId"
    }
}