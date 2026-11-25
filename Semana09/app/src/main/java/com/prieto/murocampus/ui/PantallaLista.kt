package com.prieto.murocampus.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prieto.murocampus.viewmodel.PostsViewModel
import com.prieto.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onAbrir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (val actual = estado) {

            UiState.Cargando -> {
                Cargando()
            }
            is UiState.Error -> {
                ErrorConReintento(
                    mensaje = actual.mensaje,
                    onReintentar = { vm.cargar() }
                )
            }
            is UiState.Exito -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = actual.datos,
                        key = { it.id }
                    ) { post ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAbrir(post.id)
                                }
                        ) {
                            TarjetaPost(post)
                        }
                    }
                }
            }
        }
    }
}