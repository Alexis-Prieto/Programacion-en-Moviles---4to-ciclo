package com.prieto.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.prieto.murocampus.data.PostRepositoryRemote
import com.prieto.murocampus.data.remote.RetrofitClient
import com.prieto.murocampus.ui.PantallaLista
import com.prieto.murocampus.ui.PantallaDetalle
import com.prieto.murocampus.ui.PantallaCrear
import com.prieto.murocampus.ui.theme.MuroCampusTheme
import com.prieto.murocampus.viewmodel.PostsViewModel
import com.prieto.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = PostRepositoryRemote(RetrofitClient.api)

        val factory = PostsViewModelFactory(repository)

        val vm = ViewModelProvider(
            this,
            factory
        )[PostsViewModel::class.java]

        setContent {
            MuroCampusTheme {
                var idSeleccionado by rememberSaveable {
                    mutableStateOf<Int?>(null)
                }
                var mostrarCrear by rememberSaveable {
                    mutableStateOf(false)
                }

                val volver: () -> Unit = {
                    if (mostrarCrear) {
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
                    volver()
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("MuroCampus")
                            },
                            navigationIcon = {
                                if (idSeleccionado != null || mostrarCrear) {
                                    TextButton(onClick = volver) {
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
                        if (idSeleccionado == null && !mostrarCrear) {
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
                        id != null -> {
                            PantallaDetalle(
                                vm = vm,
                                id = id,
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
        }
    }
}