package com.prieto.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.prieto.navlab.navigation.Screen
import com.prieto.navlab.ui.theme.*

@Composable
fun ProfileScreen(navController: NavController) {
    // URL de la foto de perfil para el usuario actual
    val profileImageUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=300"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextoOscuro
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Configuración de Perfil",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoOscuro
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF6C3FA9), // Violeta vibrante
                            Color(0xFF381F31)  // Ciruela / Vino oscuro
                        )
                    )
                )
                .padding(vertical = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .border(3.dp, Color.White, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFD6C8EC))
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Alexis Prieto Huiza",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = "INFORMACIÓN PERSONAL",
                color = Color(0xFF5C3C8B),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(14.dp))

            ProfileFieldRow(
                icon = Icons.Default.Person,
                label = "Nombre Completo",
                value = "Alexis Prieto Huiza"
            )
            Spacer(modifier = Modifier.height(14.dp))

            ProfileFieldRow(
                icon = Icons.Default.Email,
                label = "Correo",
                value = "alexis.prieto@tecsup.edu.pe"
            )
            Spacer(modifier = Modifier.height(14.dp))

            ProfileFieldRow(
                icon = Icons.Default.Phone,
                label = "Teléfono",
                value = "+51 987 654 321"
            )
            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "ACADÉMICO",
                color = Color(0xFF5C3C8B),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(14.dp))

            ProfileFieldRow(
                icon = Icons.Default.School,
                label = "Carrera",
                value = "Diseño y Desarrollo de Software"
            )
            Spacer(modifier = Modifier.height(14.dp))
            ProfileFieldRow(
                icon = Icons.Default.CalendarMonth,
                label = "Ciclo Actual",
                value = "VI Ciclo"
            )
            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FondoRojoLogout
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar Sesión",
                    tint = RojoLogout,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    color = RojoLogout,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Composable
private fun ProfileFieldRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF0F0F0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF5C3C8B),
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextoGris
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
        }
    }
}