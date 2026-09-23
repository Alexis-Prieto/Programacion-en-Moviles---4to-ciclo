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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

data class StudentDetail(
    val id: Int,
    val name: String,
    val career: String,
    val code: String,
    val email: String,
    val faculty: String,
    val bio: String,
    val avatarUrl: String
)

@Composable
fun DetailScreen(navController: NavController, itemId: Int) {
    val students = remember {
        listOf(
            StudentDetail(
                id = 1,
                name = "Alexis Prieto",
                career = "Ingeniería de Sistemas",
                code = "2024-0001",
                email = "alexis.prieto@example.com",
                faculty = "Ingeniería y Tecnología",
                bio = "Estudiante destacado con interés en desarrollo Android.",
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=300"
            ),
            StudentDetail(
                id = 2,
                name = "Maria Garcia",
                career = "Arquitectura",
                code = "2024-0002",
                email = "maria.garcia@example.com",
                faculty = "Arquitectura y Diseño",
                bio = "Estudiante destacada con interés en diseño urbano e interiorismo.",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=300"
            ),
            StudentDetail(
                id = 3,
                name = "Carlos Perez",
                career = "Medicina",
                code = "2024-0003",
                email = "carlos.perez@example.com",
                faculty = "Ciencias de la Salud",
                bio = "Estudiante enfocado en investigación médica y pediatría.",
                avatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=300"
            ),
            StudentDetail(
                id = 4,
                name = "Ana Lopez",
                career = "Derecho",
                code = "2024-0004",
                email = "ana.lopez@example.com",
                faculty = "Ciencias Jurídicas",
                bio = "Apasionada por el derecho internacional y corporativo.",
                avatarUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=300"
            ),
            StudentDetail(
                id = 5,
                name = "Luis Ramirez",
                career = "Administración",
                code = "2024-0005",
                email = "luis.ramirez@example.com",
                faculty = "Negocios y Gestión",
                bio = "Enfocado en la gestión de proyectos y emprendimiento digital.",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300"
            )
        )
    }

    val student = students.find { it.id == itemId } ?: students.first()
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFEDE4F7),
            Color(0xFFFAF7FC)
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
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
                        tint = Color(0xFF321A5C)
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Expediente Académico",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF321A5C)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(Color(0xFF5B439B))
            )
            AsyncImage(
                model = student.avatarUrl,
                contentDescription = student.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(110.dp)
                    .border(4.dp, Color.White, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFFD6C8EC))
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = student.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = student.career,
                fontSize = 14.sp,
                color = Color(0xFF6B53A3),
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color(0xFFECE6F0)
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                DetailItemRow(
                    icon = Icons.Default.Badge,
                    label = "ID Estudiante",
                    value = student.code
                )

                Spacer(modifier = Modifier.height(16.dp))

                DetailItemRow(
                    icon = Icons.Default.Email,
                    label = "Correo Electrónico",
                    value = student.email
                )

                Spacer(modifier = Modifier.height(16.dp))

                DetailItemRow(
                    icon = Icons.Default.School,
                    label = "Facultad",
                    value = student.faculty
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = Color(0xFFE0D8E8)
                )
                Text(
                    text = "Biografía",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D1B20)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = student.bio,
                    fontSize = 13.sp,
                    color = Color(0xFF49454F),
                    lineHeight = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun DetailItemRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF5B439B),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF79747E)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
            )
        }
    }
}