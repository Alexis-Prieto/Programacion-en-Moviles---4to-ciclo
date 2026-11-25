package com.prieto.murocampus.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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

    val usuarioFiltro by vm.usuarioFiltro.collectAsStateWithLifecycle()

    val busqueda by vm.busqueda.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (estado is UiState.Exito) {

            val colorIcono = MaterialTheme.colorScheme.onSurfaceVariant
            OutlinedTextField(
                value = busqueda,
                onValueChange = vm::actualizarBusqueda,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text("Buscar por título") },
                singleLine = true,
                leadingIcon = {
                    Canvas(modifier = Modifier.size(24.dp)) {
                        val ancho = 2.dp.toPx()
                        drawCircle(
                            color = colorIcono,
                            radius = size.minDimension * 0.27f,
                            center = Offset(size.width * 0.42f, size.height * 0.42f),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = ancho)
                        )
                        drawLine(
                            color = colorIcono,
                            start = Offset(size.width * 0.62f, size.height * 0.62f),
                            end = Offset(size.width * 0.85f, size.height * 0.85f),
                            strokeWidth = ancho
                        )
                    }
                },
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        IconButton(
                            onClick = { vm.actualizarBusqueda("") },
                            modifier = Modifier.semantics { contentDescription = "Limpiar búsqueda" }
                        ) {
                            Text("✕", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = usuarioFiltro == null,
                        onClick = { vm.filtrar(null) },
                        label = { Text("Todos") }
                    )
                }

                items((1..10).toList()) { userId ->
                    FilterChip(
                        selected = usuarioFiltro == userId,
                        onClick = { vm.filtrar(userId) },
                        label = { Text("Usuario $userId") }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
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

                    if (actual.datos.isEmpty()) {

                        Text(
                            text = if (busqueda.isNotEmpty()) {
                                "No se encontraron publicaciones"
                            } else {
                                "Sin publicaciones"
                            },
                            modifier = Modifier.align(Alignment.Center)
                        )

                    } else {
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
    }
}