package com.prieto.centrotareas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.prieto.centrotareas.model.CargaUi
import com.prieto.centrotareas.model.PrimosUi
import com.prieto.centrotareas.viewmodel.TareasViewModel

private val Azul = Color(0xFF1976B5)
private val Fondo = Color(0xFFF8F7FB)
private val Borde = Color(0xFFE0E5EC)
private val Texto = Color(0xFF252B33)
private val VerdeFondo = Color(0xFFE3F4E5)
private val VerdeTexto = Color(0xFF205C32)
private val RojoFondo = Color(0xFFFDE7E7)
private val RojoTexto = Color(0xFFB3261E)

private val ColoresCentroTareas = lightColorScheme(
    primary = Azul,
    onPrimary = Color.White,
    background = Fondo,
    onBackground = Texto,
    surface = Color.White,
    onSurface = Texto,
    surfaceVariant = Color.White,
    onSurfaceVariant = Texto
)

@Composable
private fun Tarjeta(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = BorderStroke(1.dp, Borde)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Text(
                text = titulo,
                modifier = Modifier.fillMaxWidth(),
                color = Azul,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            contenido()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {

    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()

    var toques by rememberSaveable { mutableIntStateOf(0) }

    val primos by viewModel.primos.collectAsState()
    var numero by rememberSaveable { mutableStateOf("500000") }
    var numeroCalculado by rememberSaveable { mutableStateOf<Int?>(null) }

    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()

    MaterialTheme(colorScheme = ColoresCentroTareas) {

        Scaffold(
            containerColor = Fondo,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "CentroTareas",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Azul,
                        titleContentColor = Color.White
                    )
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Tarjeta(titulo = "Tarea con progreso") {

                    Button(
                        onClick = { toques++ }
                    ) {
                        Text("Tócame: $toques")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Button(
                            onClick = { viewModel.tareaLarga() },
                            enabled = !corriendo
                        ) {
                            Text("Iniciar")
                        }

                        OutlinedButton(
                            onClick = { viewModel.cancelar() },
                            enabled = corriendo
                        ) {
                            Text("Cancelar")
                        }
                    }

                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth(),
                        color = Azul,
                        trackColor = Borde
                    )

                    Text(
                        text = "${(progreso * 100).toInt()} %",
                        color = Texto,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = mensaje,
                        color = Texto,
                        textAlign = TextAlign.Center
                    )
                }

                Tarjeta(titulo = "Contar números primos") {

                    OutlinedTextField(
                        value = numero,
                        onValueChange = { numero = it },
                        label = { Text("Hasta el número") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Button(
                            onClick = {
                                numero.toIntOrNull()?.let { n ->
                                    if (n >= 2) {
                                        numeroCalculado = n
                                        viewModel.contar(n)
                                    }
                                }
                            },
                            enabled = primos !is PrimosUi.Calculando
                        ) {
                            Text("Contar")
                        }

                        OutlinedButton(
                            onClick = { viewModel.cancelarPrimos() },
                            enabled = primos is PrimosUi.Calculando
                        ) {
                            Text("Cancelar")
                        }
                    }
                    when (val estado = primos) {
                        PrimosUi.Inicial -> Unit

                        PrimosUi.Calculando -> {
                            Text(
                                text = "Calculando...",
                                color = Texto
                            )
                        }

                        is PrimosUi.Listo -> {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = VerdeFondo,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = numeroCalculado?.let { n ->
                                            "Primos hasta $n: ${estado.cantidad}"
                                        } ?: "Primos encontrados: ${estado.cantidad}",
                                        color = VerdeTexto,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        text = "Tiempo: ${estado.milisegundos} ms",
                                        color = VerdeTexto
                                    )
                                }
                            }
                        }

                        PrimosUi.Cancelado -> {
                            Text(
                                text = "Cancelado",
                                color = Texto
                            )
                        }
                    }
                }

                Tarjeta(titulo = "Dos cargas") {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Switch(
                            checked = simularError,
                            onCheckedChange = {
                                viewModel.cambiarSimularError(it)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Simular error",
                            color = Texto
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.cargarUnaPorUna() },
                            enabled = carga !is CargaUi.Cargando
                        ) {
                            Text("Una por una")
                        }
                        Button(
                            onClick = { viewModel.cargarALaVez() },
                            enabled = carga !is CargaUi.Cargando
                        ) {
                            Text("A la vez")
                        }
                    }

                    when (val estado = carga) {
                        CargaUi.Inicial -> Unit

                        CargaUi.Cargando -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = Azul
                            )
                        }

                        is CargaUi.Exito -> {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = VerdeFondo,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Modo: ${estado.modo}",
                                        color = VerdeTexto,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        text = "Perfil: ${estado.perfil}",
                                        color = VerdeTexto
                                    )

                                    Text(
                                        text = "Pedidos: ${estado.pedidos}",
                                        color = VerdeTexto
                                    )

                                    Text(
                                        text = "Tiempo: ${estado.milisegundos} ms",
                                        color = VerdeTexto
                                    )
                                }
                            }
                        }

                        is CargaUi.Error -> {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = RojoFondo,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = estado.mensaje,
                                        color = RojoTexto,
                                        textAlign = TextAlign.Center
                                    )

                                    OutlinedButton(
                                        onClick = {
                                            viewModel.cargarALaVez()
                                        }
                                    ) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
