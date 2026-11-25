package com.prieto.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prieto.murocampus.viewmodel.PostsViewModel
import com.prieto.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    onEditar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val eliminacion by vm.eliminacion.collectAsStateWithLifecycle()
    var confirmarEliminacion by rememberSaveable(id) { mutableStateOf(false) }
    val eliminando = eliminacion == UiState.Cargando

    if (confirmarEliminacion) {
        AlertDialog(
            onDismissRequest = { confirmarEliminacion = false },
            title = { Text("¿Eliminar publicación?") },
            text = { Text("¿Estás seguro de que deseas eliminar esta publicación?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarEliminacion = false
                    vm.eliminar(id)
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarEliminacion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

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
                    onReintentar = { vm.abrir(id) }
                )
            }

            is UiState.Exito -> {
                val post = actual.datos.post
                val comentarios = actual.datos.comentarios

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Usuario ${post.userId}",
                                color = Color(0xFF17649B)
                            )

                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = post.body,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Button(
                                onClick = onEditar,
                                enabled = !eliminando && eliminacion !is UiState.Exito,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Editar publicación")
                            }

                            Button(
                                onClick = { confirmarEliminacion = true },
                                enabled = !eliminando && eliminacion !is UiState.Exito,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Eliminar publicación")
                            }

                            if (eliminando) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                            val errorEliminacion = eliminacion as? UiState.Error
                            if (errorEliminacion != null) {
                                Text(
                                    text = errorEliminacion.mensaje,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            HorizontalDivider()

                            Text(
                                text = "Comentarios (${comentarios.size})",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    items(
                        items = comentarios,
                        key = { it.id }
                    ) { comentario ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = comentario.email,
                                    color = Color(0xFF17649B),
                                    fontWeight = FontWeight.Medium
                                )

                                Text(text = comentario.body)
                            }
                        }
                    }
                }
            }
        }
    }
}