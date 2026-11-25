package com.prieto.avisoscampus.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.avisoscampus.model.PermisoUi
import com.prieto.avisoscampus.notificacion.Notificador
import com.prieto.avisoscampus.ui.theme.*
import com.prieto.avisoscampus.viewmodel.AvisosViewModel
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {

    val contexto = LocalContext.current

    val permiso by vm.permiso.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
    }
    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            Notificador.mostrar(contexto, aviso)

            // TODO Paso 8: manejar permiso denegado
        }
    }

    Scaffold(
        containerColor = FondoPantalla,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AvisosCampus",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulPrincipal,
                    titleContentColor = Blanco
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Tarjeta("Permiso de notificaciones") {

                val (textoEstado, fondoEstado, colorEstado) =
                    when (permiso) {

                        PermisoUi.SinPedir -> Triple(
                            "Sin pedir",
                            GrisEtiqueta,
                            TextoSecundario
                        )

                        PermisoUi.Concedido -> Triple(
                            "Permitido",
                            Color(0xFFE1F3E2),
                            Color(0xFF2E7D32)
                        )

                        PermisoUi.Denegado -> Triple(
                            "Denegado",
                            Color(0xFFFBE4E4),
                            Color(0xFFC62828)
                        )
                    }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Estado:")

                    Surface(
                        color = fondoEstado,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = textoEstado,
                            color = colorEstado,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            )
                        )
                    }
                }
                Button(
                    onClick = {
                        if (Notificador.tienePermiso(contexto)) {

                            vm.permisoRespondido(true)

                        } else if (
                            Build.VERSION.SDK_INT >=
                            Build.VERSION_CODES.TIRAMISU
                        ) {

                            lanzador.launch(
                                Manifest.permission.POST_NOTIFICATIONS
                            )

                        } else {

                            vm.permisoRespondido(true)
                        }
                    }
                ) {
                    Text("Permitir notificaciones")
                }
            }

            Tarjeta("Avisos") {

                Button(
                    onClick = {
                        // TODO Paso 3
                    }
                ) {
                    Text("Mostrar aviso")
                }
            }

            Tarjeta("Recordatorio") {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = {
                            // TODO Paso 5
                        }
                    ) {
                        Text("Recordar en 10 s")
                    }

                    OutlinedButton(
                        onClick = {
                            // TODO Paso 5
                        },
                        enabled = false
                    ) {
                        Text("Cancelar")
                    }
                }

                // TODO Paso 5: mostrar cuenta regresiva
            }

            Tarjeta("Receptor de anuncios") {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Escuchar el modo avión",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = false,
                        onCheckedChange = {
                            // TODO Paso 6
                        }
                    )
                }
                Text(
                    text = "Activa o desactiva el modo avión del celular",
                    color = TextoSecundario,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
            Tarjeta("Historial") {

                Text(
                    text = "Sin avisos todavía",
                    color = TextoSecundario
                )
                // TODO Paso 7: mostrar historial
            }
            // TODO Paso 8: mensaje de error y ajustes
        }
    }
}
@Composable
private fun Tarjeta(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Blanco
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BordeTarjeta
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPrincipal
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = contenido
            )
        }
    }
}