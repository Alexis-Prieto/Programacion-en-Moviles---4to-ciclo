package com.prieto.pokeapp.ui.favoritos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.ui.UiState

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val uiState = state) {
            is UiState.Cargando -> CircularProgressIndicator()
            is UiState.Error -> Text(text = uiState.mensaje)
            is UiState.Exito -> {
                val favoritos = uiState.datos
                if (favoritos.isEmpty()) {
                    Text(
                        text = "No tienes Pokémon favoritos guardados",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(favoritos, key = { it.id }) { item ->
                            // Utiliza aquí tu composable de ítem (ejemplo: ItemRow / ItemCard)
                            Text(
                                text = "${item.id}. ${item.nombre}",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}