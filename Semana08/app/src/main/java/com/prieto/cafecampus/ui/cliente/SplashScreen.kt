package com.prieto.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit
) {
    val colorNaranjaPrimario = Color(0xFFEA580C)
    val colorNaranjaContenedor = Color(0xFFFFEDD5)
    val colorNaranjaFondo = Color(0xFFFFF2E5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorNaranjaFondo)
            .padding(start = 24.dp, end = 24.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(colorNaranjaPrimario, shape = RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Coffee,
                    contentDescription = "Logo CafeCampus",
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CafeCampus",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )
            Text(
                text = "Pide sin hacer cola",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(colorNaranjaContenedor, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Coffee,
                        contentDescription = null,
                        tint = colorNaranjaPrimario,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(colorNaranjaContenedor, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Fastfood,
                        contentDescription = null,
                        tint = colorNaranjaPrimario,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(colorNaranjaContenedor, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalBar,
                        contentDescription = null,
                        tint = colorNaranjaPrimario,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorNaranjaPrimario)
            ) {
                Text(
                    text = "Comenzar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ya tengo una cuenta",
                color = colorNaranjaPrimario,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}