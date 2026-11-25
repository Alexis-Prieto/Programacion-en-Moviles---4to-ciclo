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
                val volver: () -> Unit = {
                    vm.cerrarDetalle()
                    idSeleccionado = null
                }
                BackHandler(enabled = idSeleccionado != null) {
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
                                if (idSeleccionado != null) {
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
                    }
                ) { innerPadding ->
                    val id = idSeleccionado

                    if (id == null) {
                        PantallaLista(
                            vm = vm,
                            onAbrir = { postId ->
                                vm.abrir(postId)
                                idSeleccionado = postId
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        PantallaDetalle(
                            vm = vm,
                            id = id,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}