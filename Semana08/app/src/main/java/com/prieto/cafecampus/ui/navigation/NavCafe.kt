package com.prieto.cafecampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prieto.cafecampus.data.prefs.PreferenciasStore
import com.prieto.cafecampus.data.repository.CafeRepository
import com.prieto.cafecampus.ui.cliente.LoginScreen
import com.prieto.cafecampus.ui.cliente.SplashScreen
import com.prieto.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(repository: CafeRepository) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefsStore = remember { PreferenciasStore(context) }

    val sesionViewModel: SesionViewModel = viewModel(
        factory = SesionViewModel.Factory(repository, prefsStore)
    )
    val perfilActivo by sesionViewModel.perfilActivo.collectAsState()

    LaunchedEffect(perfilActivo) {
        if (perfilActivo == "CLIENTE") {
            navController.navigate("cliente_home") {
                popUpTo("splash") { inclusive = true }
            }
        } else if (perfilActivo == "ADMIN") {
            navController.navigate("admin_home") {
                popUpTo("splash") { inclusive = true }
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
                    navController.navigate("login")
                }
            )
        }
        composable("login") {
            LoginScreen(
                viewModel = sesionViewModel,
                onLoginSuccess = { rol ->
                    if (rol == "CLIENTE") {
                        navController.navigate("cliente_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else if (rol == "ADMIN") {
                        navController.navigate("admin_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }
        composable("cliente_home") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Menú de Cliente (En desarrollo)")
            }
        }
        composable("admin_home") {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Panel Administrador (En desarrollo)")
            }
        }
    }
}