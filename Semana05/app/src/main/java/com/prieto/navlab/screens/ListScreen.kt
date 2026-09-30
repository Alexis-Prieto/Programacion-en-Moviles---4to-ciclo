package com.prieto.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.prieto.navlab.navigation.Screen

data class StudentData(
    val id: Int,
    val name: String,
    val career: String,
    val code: String = "",
    val avatarUrl: String = ""
)
@Composable
fun ListScreen(navController: NavController) {
    val students = remember {
        listOf(
            StudentData(1, "Alexis Prieto", "Ingeniería de Sistemas", "20210001", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150"),
            StudentData(2, "Maria Garcia", "Arquitectura", "20210002", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150"),
            StudentData(3, "Carlos Perez", "Medicina", "20210003", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150"),
            StudentData(4, "Ana Lopez", "Derecho", "20210004", "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150"),
            StudentData(5, "Luis Ramirez", "Administración", "20210005", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150")
        )
    }
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
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFE2D5F3)
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
                    text = "Directorio de Alumnos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF321A5C)
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(students) { student ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.Detail.createRoute(student.id))
                        },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color(0xFFECE6F0)
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = student.avatarUrl,
                            contentDescription = student.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = student.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1D1B20)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = student.career,
                                fontSize = 13.sp,
                                color = Color(0xFF6B53A3),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFF79747E)
                        )
                    }
                }
            }
        }
    }
}