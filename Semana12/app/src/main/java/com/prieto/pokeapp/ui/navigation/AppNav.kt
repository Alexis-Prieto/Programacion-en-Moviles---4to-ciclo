package com.prieto.pokeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prieto.pokeapp.AppContainer
import com.prieto.pokeapp.AppViewModelFactory
import com.prieto.pokeapp.domain.model.Rol
import com.prieto.pokeapp.ui.UiState
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
    val factory = remember(appContainer) { AppViewModelFactory(appContainer) }
    val sessionViewModel: LoginViewModel = viewModel(factory = factory)
    val estadoSesion by sessionViewModel.sesion.collectAsStateWithLifecycle()
    when (val state = estadoSesion) {
        UiState.Cargando -> {
            CircularProgressIndicator()
            return
        }
        is UiState.Error -> {
            Column {
                Text(state.mensaje)
                Button(onClick = sessionViewModel::reintentarSesion) { Text("Reintentar") }
            }
            return
        }
        is UiState.Exito -> Unit
    }
    val sesion = (estadoSesion as UiState.Exito).datos
    val userRole = sesion?.rol ?: Rol.USUARIO
    val destinoInicial = rememberSaveable { if (sesion == null) "login" else "lista" }

    val onNavigate: (String) -> Unit = { route ->
        if (route == "admin" && userRole != Rol.ADMIN) {
            navController.navigate("lista") {
                popUpTo("lista") { inclusive = true }
            }
        } else {
            navController.navigate(route) {
                popUpTo("lista") {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = destinoInicial
    ) {
        composable("login") {
            LoginScreen(
                viewModel = sessionViewModel,
                onLoginExitoso = { sesion ->
                    sessionViewModel.aceptarSesion(sesion)
                    navController.navigate("lista") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("admin") {
            if (userRole != Rol.ADMIN) {
                LaunchedEffect(Unit) {
                    val destino = if (sesion == null) "login" else "lista"
                    navController.navigate(destino) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                return@composable
            }

            val adminViewModel: AdminViewModel = viewModel(factory = factory)
            AdminScreen(
                viewModel = adminViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onIrAlCatalogo = {
                    onNavigate("lista")
                },
                onCerrarSesion = {
                    sessionViewModel.cerrarSesion()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigate = onNavigate
            )
        }

        composable("lista") {
            val listaViewModel: ListaViewModel = viewModel(factory = factory)
            ListaScreen(
                viewModel = listaViewModel,
                onItemClick = { id ->
                    navController.navigate("detalle/$id")
                },
                onNavigate = onNavigate,
                onCerrarSesion = {
                    sessionViewModel.cerrarSesion()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                esAdmin = (userRole == Rol.ADMIN)
            )
        }

        composable("favoritos") {
            val favoritosViewModel: FavoritosViewModel = viewModel(factory = factory)
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
                factory = AppViewModelFactory(appContainer, id)
            )
            DetalleScreen(
                viewModel = detalleViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
