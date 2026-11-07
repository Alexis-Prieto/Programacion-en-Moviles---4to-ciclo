package com.prieto.pokeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prieto.pokeapp.AppContainer
import com.prieto.pokeapp.domain.model.Rol
import com.prieto.pokeapp.ui.admin.AdminScreen
import com.prieto.pokeapp.ui.admin.AdminViewModel
import com.prieto.pokeapp.ui.detalle.DetalleScreen
import com.prieto.pokeapp.ui.detalle.DetalleViewModel
import com.prieto.pokeapp.ui.favoritos.FavoritosScreen
import com.prieto.pokeapp.ui.favoritos.FavoritosViewModel
import com.prieto.pokeapp.ui.lista.ListaScreen
import com.prieto.pokeapp.ui.lista.ListaViewModel
import com.prieto.pokeapp.ui.login.LoginScreen
import com.prieto.pokeapp.ui.login.LoginViewModel

@Composable
fun AppNav(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    var userRole by remember { mutableStateOf(Rol.USUARIO) }

    val onNavigate: (String) -> Unit = { route ->
        if (route == "admin" && userRole != Rol.ADMIN) {
            navController.navigate("lista") {
                popUpTo("lista") { inclusive = true }
            }
        } else {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            val loginViewModel: LoginViewModel = viewModel {
                LoginViewModel(appContainer.authRepository)
            }
            LoginScreen(
                viewModel = loginViewModel,
                onLoginExitoso = { sesion ->
                    userRole = sesion.rol
                    val destino = if (sesion.rol == Rol.ADMIN) "admin" else "lista"
                    navController.navigate(destino) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("admin") {
            if (userRole != Rol.ADMIN) {
                LaunchedEffect(Unit) {
                    val destino = if (appContainer.authRepository.sesionActual() == null) "login" else "lista"
                    navController.navigate(destino) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                return@composable
            }

            val adminViewModel: AdminViewModel = viewModel {
                AdminViewModel(appContainer.itemRepository, appContainer.notificador)
            }
            AdminScreen(
                viewModel = adminViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onIrAlCatalogo = {
                    navController.navigate("lista")
                },
                onCerrarSesion = {
                    appContainer.authRepository.cerrarSesion()
                    userRole = Rol.USUARIO
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigate = onNavigate
            )
        }

        composable("lista") {
            val listaViewModel: ListaViewModel = viewModel {
                ListaViewModel(appContainer.itemRepository, appContainer.notificador)
            }
            ListaScreen(
                viewModel = listaViewModel,
                onItemClick = { id ->
                    navController.navigate("detalle/$id")
                },
                onNavigate = onNavigate,
                onCerrarSesion = {
                    appContainer.authRepository.cerrarSesion()
                    userRole = Rol.USUARIO
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                esAdmin = (userRole == Rol.ADMIN)
            )
        }

        composable("favoritos") {
            val favoritosViewModel: FavoritosViewModel = viewModel {
                FavoritosViewModel(appContainer.itemRepository)
            }
            FavoritosScreen(
                viewModel = favoritosViewModel,
                onItemClick = { id ->
                    navController.navigate("detalle/$id")
                },
                onNavigate = onNavigate,
                esAdmin = (userRole == Rol.ADMIN)
            )
        }

        composable("detalle/{id}") {
            val backStackEntry = navController.currentBackStackEntry
            val idStr = backStackEntry?.arguments?.getString("id") ?: "1"
            val id = idStr.toIntOrNull() ?: 1
            val detalleViewModel: DetalleViewModel = viewModel(
                factory = DetalleViewModel.Factory(appContainer.itemRepository, id)
            )
            DetalleScreen(
                viewModel = detalleViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
