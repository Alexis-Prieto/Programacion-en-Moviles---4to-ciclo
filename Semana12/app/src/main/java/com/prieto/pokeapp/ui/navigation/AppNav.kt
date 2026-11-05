package com.prieto.pokeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prieto.pokeapp.AppContainer
import com.prieto.pokeapp.ui.admin.AdminScreen
import com.prieto.pokeapp.ui.admin.AdminViewModel
import com.prieto.pokeapp.ui.login.LoginScreen
import com.prieto.pokeapp.ui.login.LoginViewModel

@Composable
fun AppNav(
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController()
) {
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
                onLoginExitoso = {
                    navController.navigate("admin") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("admin") {
            val adminViewModel: AdminViewModel = viewModel {
                AdminViewModel(appContainer.itemRepository)
            }
            AdminScreen(
                viewModel = adminViewModel,
                onCerrarSesion = {
                    appContainer.authRepository.cerrarSesion()
                    navController.navigate("login") {
                        popUpTo("admin") { inclusive = true }
                    }
                }
            )
        }
    }
}
