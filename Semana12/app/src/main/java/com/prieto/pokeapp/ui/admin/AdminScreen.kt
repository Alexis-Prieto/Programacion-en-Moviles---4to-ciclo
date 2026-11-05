package com.prieto.pokeapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prieto.pokeapp.ui.UiState

@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    onCerrarSesion: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Panel de Administración",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = viewModel::limpiarCache,
            enabled = uiState !is UiState.Cargando,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Limpiar Caché Local")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onCerrarSesion
        ) {
            Text("Cerrar Sesión")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (uiState) {
            is UiState.Cargando -> CircularProgressIndicator()
            is UiState.Exito -> Text(
                text = (uiState as UiState.Exito<String>).datos,
                color = MaterialTheme.colorScheme.primary
            )
            is UiState.Error -> Text(
                text = (uiState as UiState.Error).mensaje,
                color = MaterialTheme.colorScheme.error
            )
            is UiState.Idle, null -> {}
        }
    }
}
