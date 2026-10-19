package com.prieto.cafecampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository
import com.prieto.cafecampus.ui.cliente.ConfirmarPedidoScreen
import com.prieto.cafecampus.ui.cliente.DetalleProductoScreen
import com.prieto.cafecampus.ui.cliente.LoginScreen
import com.prieto.cafecampus.ui.cliente.MenuScreen
import com.prieto.cafecampus.ui.cliente.MisPedidosScreen
import com.prieto.cafecampus.ui.cliente.SeguimientoScreen
import com.prieto.cafecampus.ui.cliente.SplashScreen
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.PedidosClienteViewModel
import com.prieto.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(repository: CafeRepository) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val store = remember { PreferenciasStore(context) }
    val sesionViewModel = remember { SesionViewModel(repository, store) }
    val menuViewModel = remember { MenuViewModel(repository) }

    val clienteId = 1
    val pedidosViewModel = remember { PedidosClienteViewModel(repository, clienteId) }

    fun navegarPestana(ruta: String) {
        if (ruta == "cliente_home") {
            val regresado = navController.popBackStack("cliente_home", inclusive = false)
            if (!regresado) {
                navController.navigate("cliente_home") {
                    launchSingleTop = true
                }
            }
        } else {
            navController.navigate(ruta) {
                popUpTo("cliente_home") { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                viewModel = sesionViewModel,
                onLoginSuccess = { rol ->
                    val destino = if (rol.uppercase() == "ADMIN") "admin_home" else "cliente_home"
                    navController.navigate(destino) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("cliente_home") {
            MenuScreen(
                vm = menuViewModel,
                onProducto = { productoId ->
                    navController.navigate("detalle_producto/$productoId")
                },
                onCarrito = {
                    navController.navigate("confirmar_pedido")
                },
                onPedidos = {
                    navegarPestana("mis_pedidos")
                },
                onPerfil = {
                    navegarPestana("perfil")
                }
            )
        }
        composable("admin_home") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Panel Administrador (En desarrollo)")
            }
        }
        composable(
            route = "detalle_producto/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            DetalleProductoScreen(
                productoId = productoId,
                menuViewModel = menuViewModel,
                onVolver = { navController.popBackStack() },
                onProductoAgregado = { navController.popBackStack() }
            )
        }
        composable("confirmar_pedido") {
            ConfirmarPedidoScreen(
                menuViewModel = menuViewModel,
                pedidosViewModel = pedidosViewModel,
                onVolver = { navController.popBackStack() },
                onPedidoConfirmado = { nuevoPedidoId ->
                    navController.navigate("seguimiento/$nuevoPedidoId") {
                        popUpTo("cliente_home") { inclusive = false }
                    }
                }
            )
        }
        composable(
            route = "seguimiento/{pedidoId}",
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: 0
            SeguimientoScreen(
                pedidoId = pedidoId,
                pedidosViewModel = pedidosViewModel,
                menuViewModel = menuViewModel,
                onVolver = { navController.popBackStack() },
                onNavigateMenu = { navegarPestana("cliente_home") },
                onNavigatePedidos = { navegarPestana("mis_pedidos") },
                onNavigatePerfil = { navegarPestana("perfil") }
            )
        }
        composable("mis_pedidos") {
            MisPedidosScreen(
                pedidosViewModel = pedidosViewModel,
                menuViewModel = menuViewModel,
                onVolver = { navController.popBackStack() },
                onPedidoClick = { id -> navController.navigate("seguimiento/$id") },
                onNavigateMenu = { navegarPestana("cliente_home") },
                onNavigatePerfil = { navegarPestana("perfil") }
            )
        }
        composable("perfil") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Perfil de usuario (En desarrollo)")
            }
        }
    }
}