package com.prieto.cafecampus.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository
import com.prieto.cafecampus.ui.admin.AjustesAdminScreen
import com.prieto.cafecampus.ui.admin.DetallePedidoAdminScreen
import com.prieto.cafecampus.ui.admin.FormularioProductoScreen
import com.prieto.cafecampus.ui.admin.MenuAdminScreen
import com.prieto.cafecampus.ui.admin.PanelScreen
import com.prieto.cafecampus.ui.admin.PedidosAdminScreen
import com.prieto.cafecampus.ui.cliente.ConfirmarPedidoScreen
import com.prieto.cafecampus.ui.cliente.DetalleProductoScreen
import com.prieto.cafecampus.ui.cliente.LoginScreen
import com.prieto.cafecampus.ui.cliente.MenuScreen
import com.prieto.cafecampus.ui.cliente.MisPedidosScreen
import com.prieto.cafecampus.ui.cliente.PerfilScreen
import com.prieto.cafecampus.ui.cliente.RegistroScreen
import com.prieto.cafecampus.ui.cliente.SeguimientoScreen
import com.prieto.cafecampus.ui.cliente.SplashScreen
import com.prieto.cafecampus.ui.theme.CafeCampusTheme
import com.prieto.cafecampus.viewmodel.AjustesAdminViewModel
import com.prieto.cafecampus.viewmodel.CafeViewModelFactory
import com.prieto.cafecampus.viewmodel.MenuAdminViewModel
import com.prieto.cafecampus.viewmodel.MenuViewModel
import com.prieto.cafecampus.viewmodel.PanelAdminViewModel
import com.prieto.cafecampus.viewmodel.PedidosAdminViewModel
import com.prieto.cafecampus.viewmodel.PedidosClienteViewModel
import com.prieto.cafecampus.viewmodel.PerfilViewModel
import com.prieto.cafecampus.viewmodel.RegistroViewModel
import com.prieto.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(repository: CafeRepository) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val store = remember { PreferenciasStore(context) }

    val factory = remember { CafeViewModelFactory(repository, store) }

    val esModoOscuro by store.modoOscuro.collectAsState(initial = false)

    val sesionViewModel: SesionViewModel = viewModel(factory = factory)
    val registroViewModel: RegistroViewModel = viewModel(factory = factory)
    val menuViewModel: MenuViewModel = viewModel(factory = factory)
    val pedidosViewModel: PedidosClienteViewModel = viewModel(factory = factory)
    val panelAdminViewModel: PanelAdminViewModel = viewModel(factory = factory)
    val ajustesAdminViewModel: AjustesAdminViewModel = viewModel(factory = factory)
    val pedidosAdminViewModel: PedidosAdminViewModel = viewModel(factory = factory)
    val menuAdminViewModel: MenuAdminViewModel = viewModel(factory = factory)
    val perfilViewModel: PerfilViewModel = viewModel(factory = factory)

    fun navegarPestana(ruta: String) {
        if (ruta == Ruta.ClienteHome.ruta) {
            val regresado = navController.popBackStack(Ruta.ClienteHome.ruta, inclusive = false)
            if (!regresado) {
                navController.navigate(Ruta.ClienteHome.ruta) { launchSingleTop = true }
            }
        } else {
            navController.navigate(ruta) {
                popUpTo(Ruta.ClienteHome.ruta) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
    CafeCampusTheme(darkTheme = esModoOscuro) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = Ruta.Splash.ruta
            ) {
                composable(Ruta.Splash.ruta) {
                    SplashScreen(
                        viewModel = sesionViewModel,
                        onNavigateToLogin = {
                            navController.navigate(Ruta.Login.ruta) {
                                popUpTo(Ruta.Splash.ruta) { inclusive = true }
                            }
                        },
                        onNavigateToAdmin = {
                            navController.navigate(Ruta.AdminHome.ruta) {
                                popUpTo(Ruta.Splash.ruta) { inclusive = true }
                            }
                        },
                        onNavigateToCliente = {
                            navController.navigate(Ruta.ClienteHome.ruta) {
                                popUpTo(Ruta.Splash.ruta) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Ruta.Login.ruta) {
                    LoginScreen(
                        viewModel = sesionViewModel,
                        onLoginSuccess = { rol ->
                            val destino = if (rol.uppercase() == "ADMIN") Ruta.AdminHome.ruta else Ruta.ClienteHome.ruta
                            navController.navigate(destino) {
                                popUpTo(Ruta.Login.ruta) { inclusive = true }
                            }
                        },
                        onNavigateToRegistro = {
                            navController.navigate(Ruta.Registro.ruta)
                        }
                    )
                }
                composable(Ruta.Registro.ruta) {
                    RegistroScreen(
                        viewModel = registroViewModel,
                        onRegistroExitoso = {
                            navController.popBackStack()
                        },
                        onVolverALogin = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(route = Ruta.ClienteHome.ruta) {
                    MenuScreen(
                        vm = menuViewModel,
                        onProducto = { productoId ->
                            navController.navigate(Ruta.DetalleProducto.crearRuta(productoId))
                        },
                        onCarrito = {
                            navController.navigate(Ruta.ConfirmarPedido.ruta)
                        },
                        onPedidos = {
                            navegarPestana(Ruta.MisPedidos.ruta)
                        },
                        onPerfil = {
                            navegarPestana(Ruta.Perfil.ruta)
                        }
                    )
                }
                composable(Ruta.Perfil.ruta) {
                    PerfilScreen(
                        perfilViewModel = perfilViewModel,
                        menuViewModel = menuViewModel,
                        onMenu = { navegarPestana(Ruta.ClienteHome.ruta) },
                        onPedidos = { navegarPestana(Ruta.MisPedidos.ruta) },
                        onCerrarSesionExitosa = {
                            navController.navigate(Ruta.Login.ruta) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Ruta.AdminHome.ruta) {
                    PanelScreen(
                        panelViewModel = panelAdminViewModel,
                        menuViewModel = menuViewModel,
                        onNavigatePedidos = { navController.navigate(Ruta.AdminPedidos.ruta) },
                        onNavigateMenu = { navController.navigate(Ruta.AdminMenu.ruta) },
                        onNavigateAjustes = { navController.navigate(Ruta.AdminAjustes.ruta) }
                    )
                }
                composable(Ruta.AdminPedidos.ruta) {
                    PedidosAdminScreen(
                        pedidosAdminViewModel = pedidosAdminViewModel,
                        menuViewModel = menuViewModel,
                        onVolver = { navController.popBackStack() },
                        onPedidoClick = { id -> navController.navigate(Ruta.AdminDetallePedido.crearRuta(id)) },
                        onNavigatePanel = { navController.popBackStack(Ruta.AdminHome.ruta, inclusive = false) },
                        onNavigateMenu = { navController.navigate(Ruta.AdminMenu.ruta) },
                        onNavigateAjustes = { navController.navigate(Ruta.AdminAjustes.ruta) }
                    )
                }
                composable(Ruta.AdminMenu.ruta) {
                    MenuAdminScreen(
                        menuAdminViewModel = menuAdminViewModel,
                        onVolver = { navController.popBackStack() },
                        onNuevoProducto = { navController.navigate(Ruta.AdminFormularioProducto.crearRuta(0)) },
                        onEditarProducto = { productoId -> navController.navigate(Ruta.AdminFormularioProducto.crearRuta(productoId)) },
                        onNavigatePanel = { navController.popBackStack(Ruta.AdminHome.ruta, inclusive = false) },
                        onNavigatePedidos = { navController.navigate(Ruta.AdminPedidos.ruta) },
                        onNavigateAjustes = { navController.navigate(Ruta.AdminAjustes.ruta) }
                    )
                }
                composable(Ruta.AdminAjustes.ruta) {
                    AjustesAdminScreen(
                        viewModel = ajustesAdminViewModel,
                        onCambiarPerfilCliente = {
                            navController.navigate(Ruta.ClienteHome.ruta) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onCerrarSesion = {
                            menuViewModel.vaciar()
                            navController.navigate(Ruta.Login.ruta) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigatePanel = { navController.popBackStack(Ruta.AdminHome.ruta, inclusive = false) },
                        onNavigatePedidos = { navController.navigate(Ruta.AdminPedidos.ruta) },
                        onNavigateMenu = { navController.navigate(Ruta.AdminMenu.ruta) }
                    )
                }
                composable(
                    route = Ruta.AdminFormularioProducto.ruta,
                    arguments = listOf(navArgument("productoId") {
                        type = NavType.IntType
                        defaultValue = 0
                    })
                ) { backStackEntry ->
                    val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
                    FormularioProductoScreen(
                        productoId = productoId,
                        menuAdminViewModel = menuAdminViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Ruta.AdminDetallePedido.ruta,
                    arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: 0
                    DetallePedidoAdminScreen(
                        pedidoId = pedidoId,
                        pedidosAdminViewModel = pedidosAdminViewModel,
                        menuViewModel = menuViewModel,
                        onVolver = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Ruta.DetalleProducto.ruta,
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
                composable(Ruta.ConfirmarPedido.ruta) {
                    ConfirmarPedidoScreen(
                        menuViewModel = menuViewModel,
                        pedidosViewModel = pedidosViewModel,
                        onVolver = { navController.popBackStack() },
                        onPedidoConfirmado = { nuevoPedidoId ->
                            navController.navigate(Ruta.Seguimiento.crearRuta(nuevoPedidoId)) {
                                popUpTo(Ruta.ClienteHome.ruta) { inclusive = false }
                            }
                        }
                    )
                }
                composable(
                    route = Ruta.Seguimiento.ruta,
                    arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: 0
                    SeguimientoScreen(
                        pedidoId = pedidoId,
                        pedidosViewModel = pedidosViewModel,
                        menuViewModel = menuViewModel,
                        onVolver = { navController.popBackStack() },
                        onNavigateMenu = { navegarPestana(Ruta.ClienteHome.ruta) },
                        onNavigatePedidos = { navegarPestana(Ruta.MisPedidos.ruta) },
                        onNavigatePerfil = { navegarPestana(Ruta.Perfil.ruta) }
                    )
                }
                composable(Ruta.MisPedidos.ruta) {
                    MisPedidosScreen(
                        pedidosViewModel = pedidosViewModel,
                        menuViewModel = menuViewModel,
                        onVolver = { navController.popBackStack() },
                        onPedidoClick = { id -> navController.navigate(Ruta.Seguimiento.crearRuta(id)) },
                        onNavigateMenu = { navegarPestana(Ruta.ClienteHome.ruta) },
                        onNavigatePerfil = { navegarPestana(Ruta.Perfil.ruta) }
                    )
                }
            }
        }
    }
}