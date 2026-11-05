package com.prieto.pokeapp.ui.detalle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prieto.pokeapp.data.remote.dto.ItemDetailDto
import com.prieto.pokeapp.ui.UiState
import com.prieto.pokeapp.ui.components.Cargando
import com.prieto.pokeapp.ui.components.ErrorView

@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Idle -> {}
        is UiState.Cargando -> Cargando(modifier = modifier)
        is UiState.Error -> ErrorView(
            mensaje = state.mensaje,
            onReintentar = { viewModel.cargarDetalle() },
            modifier = modifier
        )
        is UiState.Exito -> DetalleContenido(
            detalle = state.datos,
            modifier = modifier
        )
    }
}

@Composable
fun DetalleContenido(
    detalle: ItemDetailDto,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "#${detalle.id} ${detalle.nombre.replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Altura: ${detalle.altura} dm",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Peso: ${detalle.peso} hg",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
