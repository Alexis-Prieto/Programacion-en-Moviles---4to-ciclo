package com.prieto.pokeapp.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.ui.UiState
import com.prieto.pokeapp.ui.components.Cargando
import com.prieto.pokeapp.ui.components.ErrorView

@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is UiState.Idle -> {}
        is UiState.Cargando -> {
            Cargando(modifier = modifier)
        }
        is UiState.Error -> {
            ErrorView(
                mensaje = state.mensaje,
                onReintentar = { viewModel.refrescar() },
                modifier = modifier
            )
        }
        is UiState.Exito -> {
            ListaContenido(
                items = state.datos,
                onFavoritoToggle = { id, esFav -> viewModel.cambiarFavorito(id, esFav) },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ListaContenido(
    items: List<Item>,
    onFavoritoToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it.id }) { item ->
            ItemRow(
                item = item,
                onFavoritoClick = { onFavoritoToggle(item.id, !item.esFavorito) }
            )
        }
    }
}

@Composable
private fun ItemRow(
    item: Item,
    onFavoritoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "#${item.id} ${item.nombre.replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            IconButton(onClick = onFavoritoClick) {
                Icon(
                    imageVector = if (item.esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (item.esFavorito) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
