package com.prieto.cafecampus.ui.cliente

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prieto.cafecampus.viewmodel.SesionViewModel

private val ColorNaranjaPrimario = Color(0xFFEA580C)
private val ColorNaranjaContenedor = Color(0xFFFFEDD5)
private val ColorNaranjaFondo = Color.White

private val ColorIndigoPrimario = Color(0xFF4F46E5)
private val ColorIndigoContenedor = Color(0xFFE0E7FF)
private val ColorIndigoFondo = Color.White

@Composable
fun LoginScreen(
    viewModel: SesionViewModel,
    onLoginSuccess: (String) -> Unit
) {
    var rolSeleccionado by remember { mutableStateOf("Estudiante") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    val errorState by viewModel.error.collectAsState()

    val colorPrimario by animateColorAsState(
        targetValue = if (rolSeleccionado == "Estudiante") ColorNaranjaPrimario else ColorIndigoPrimario,
        label = "colorPrimario"
    )
    val colorFondo by animateColorAsState(
        targetValue = if (rolSeleccionado == "Estudiante") ColorNaranjaFondo else ColorIndigoFondo,
        label = "colorFondo"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Bienvenido",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Ingresa para continuar",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE5E7EB), shape = RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = if (rolSeleccionado == "Estudiante") ColorNaranjaPrimario else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        rolSeleccionado = "Estudiante"
                        viewModel.limpiarError()
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Estudiante",
                    fontWeight = FontWeight.Bold,
                    color = if (rolSeleccionado == "Estudiante") Color.White else Color(0xFF4B5563)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = if (rolSeleccionado == "Administrador") ColorIndigoPrimario else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        rolSeleccionado = "Administrador"
                        viewModel.limpiarError()
                    }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Administrador",
                    fontWeight = FontWeight.Bold,
                    color = if (rolSeleccionado == "Administrador") Color.White else Color(0xFF4B5563)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                viewModel.limpiarError()
            },
            label = { Text("Correo") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Correo",
                    tint = colorPrimario
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorPrimario,
                focusedLabelColor = colorPrimario,
                unfocusedBorderColor = Color(0xFFD1D5DB)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = {
                clave = it
                viewModel.limpiarError()
            },
            label = { Text("Contraseña") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Contraseña",
                    tint = colorPrimario
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorPrimario,
                focusedLabelColor = colorPrimario,
                unfocusedBorderColor = Color(0xFFD1D5DB)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = colorPrimario,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.End)
        )

        if (errorState != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorState!!,
                color = Color(0xFFDC2626),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.login(correo, clave, rolSeleccionado) { rolLogueado ->
                    onLoginSuccess(rolLogueado)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorPrimario)
        ) {
            Text(
                text = "Ingresar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (rolSeleccionado == "Estudiante") {
            Row {
                Text(text = "¿No tienes cuenta? ", color = Color(0xFF6B7280), fontSize = 13.sp)
                Text(
                    text = "Regístrate",
                    color = ColorNaranjaPrimario,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ColorIndigoContenedor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Modo práctica",
                        fontWeight = FontWeight.Bold,
                        color = ColorIndigoPrimario,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                        color = Color(0xFF374151),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}