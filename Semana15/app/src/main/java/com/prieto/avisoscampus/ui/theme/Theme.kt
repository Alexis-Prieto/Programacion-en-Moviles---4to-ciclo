package com.prieto.avisoscampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColoresAvisos = lightColorScheme(
    primary = AzulPrincipal,
    onPrimary = Blanco,
    background = FondoPantalla,
    onBackground = TextoPrincipal,
    surface = Blanco,
    onSurface = TextoPrincipal,
    outline = BordeTarjeta
)

@Composable
fun AvisosCampusTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColoresAvisos,
        typography = Typography,
        content = content
    )
}