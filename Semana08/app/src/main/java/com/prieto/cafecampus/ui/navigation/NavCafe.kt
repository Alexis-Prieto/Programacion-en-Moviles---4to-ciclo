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
import com.prieto.cafecampus.ui.cliente.DetalleProductoScreen
import com.prieto.cafecampus.ui.cliente.LoginScreen
import com.prieto.cafecampus.ui.cliente.MenuScreen
import com.prieto.cafecampus.ui.cliente.SplashScreen
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(repository: CafeRepository) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val store = remember { PreferenciasStore(context) }
    val sesionViewModel = remember { SesionViewModel(repository, store) }
    val menuViewModel = remember { MenuViewModel(repository) }

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
    }
}