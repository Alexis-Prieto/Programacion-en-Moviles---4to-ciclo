package com.prieto.murocampus.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prieto.murocampus.ui.PantallaLista
import com.prieto.murocampus.ui.PantallaDetalle
import com.prieto.murocampus.ui.PantallaCrear
import com.prieto.murocampus.ui.PantallaEditar
import com.prieto.murocampus.viewmodel.PostsViewModel
import com.prieto.murocampus.viewmodel.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav(vm: PostsViewModel) {

    val estadoLista by vm.lista.collectAsStateWithLifecycle()
    val estadoEliminacion by vm.eliminacion.collectAsStateWithLifecycle()
    val estadoEdicion by vm.edicion.collectAsStateWithLifecycle()
    val estadoDetalle by vm.detalle.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var idSeleccionado by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    var mostrarCrear by rememberSaveable {
        mutableStateOf(false)
    }
    var mostrarEditar by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(estadoEliminacion) {
        if (estadoEliminacion is UiState.Exito && idSeleccionado != null) {
            vm.cerrarDetalle()
            idSeleccionado = null
            scope.launch { snackbarHostState.showSnackbar("Publicación eliminada") }
        }
    }

    val volver: () -> Unit = {
        if (mostrarEditar) {
            vm.limpiarEdicion()
            mostrarEditar = false
        } else if (mostrarCrear) {
            vm.limpiarPublicacion()
            mostrarCrear = false
        } else {
            vm.cerrarDetalle()
            idSeleccionado = null
        }
    }
    BackHandler(
        enabled = idSeleccionado != null || mostrarCrear
    ) {
        if (estadoEliminacion != UiState.Cargando && estadoEdicion != UiState.Cargando) volver()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),

        snackbarHost = { SnackbarHost(snackbarHostState) },

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MuroCampus",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (idSeleccionado != null || mostrarCrear) {
                        TextButton(
                            onClick = volver,
                            enabled = estadoEliminacion != UiState.Cargando &&
                                    estadoEdicion != UiState.Cargando
                        ) {
                            Text(
                                text = "Atrás",
                                color = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF17649B),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (
                idSeleccionado == null &&
                !mostrarCrear &&
                estadoLista is UiState.Exito
            ) {
                FloatingActionButton(
                    onClick = {
                        vm.limpiarPublicacion()
                        mostrarCrear = true
                    },
                    containerColor = Color(0xFF17649B),
                    contentColor = Color.White
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    ) { innerPadding ->

        val id = idSeleccionado

        when {
            mostrarCrear -> {
                PantallaCrear(
                    vm = vm,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            mostrarEditar && id != null -> {
                val detalle = estadoDetalle
                if (detalle is UiState.Exito && detalle.datos.post.id == id) {
                    PantallaEditar(
                        vm = vm,
                        post = detalle.datos.post,
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    PantallaDetalle(
                        vm = vm,
                        id = id,
                        onEditar = {},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
            id != null -> {
                PantallaDetalle(
                    vm = vm,
                    id = id,
                    onEditar = {
                        vm.limpiarEdicion()
                        mostrarEditar = true
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            else -> {
                PantallaLista(
                    vm = vm,
                    onAbrir = { postId ->
                        vm.abrir(postId)
                        idSeleccionado = postId
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}