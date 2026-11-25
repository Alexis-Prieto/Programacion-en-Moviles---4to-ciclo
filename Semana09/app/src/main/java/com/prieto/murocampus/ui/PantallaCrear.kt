package com.prieto.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prieto.murocampus.viewmodel.PostsViewModel
import com.prieto.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(
    vm: PostsViewModel,
    modifier: Modifier = Modifier
) {
    var titulo by rememberSaveable {
        mutableStateOf("")
    }

    var texto by rememberSaveable {
        mutableStateOf("")
    }
    val estado by vm.publicacion.collectAsStateWithLifecycle()

    val publicando = estado == UiState.Cargando

    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            texto = ""
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Nueva publicación",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = titulo,
            onValueChange = {
                if (estado is UiState.Exito) {
                    vm.limpiarPublicacion()
                }
                titulo = it
            },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !publicando
        )
        OutlinedTextField(
            value = texto,
            onValueChange = {
                if (estado is UiState.Exito) {
                    vm.limpiarPublicacion()
                }
                texto = it
            },
            label = { Text("Contenido") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            enabled = !publicando
        )

        Button(
            onClick = {
                vm.publicar(titulo, texto)
            },
            enabled = titulo.isNotBlank() &&
                    texto.isNotBlank() &&
                    !publicando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Publicar")
        }

        when (val actual = estado) {

            null -> Unit

            UiState.Cargando -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )

                Text(
                    text = "Publicando...",
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
            }
            is UiState.Exito -> {
                Text(
                    text = "Publicado con id ${actual.datos.id}",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }
            is UiState.Error -> {
                Text(
                    text = actual.mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}