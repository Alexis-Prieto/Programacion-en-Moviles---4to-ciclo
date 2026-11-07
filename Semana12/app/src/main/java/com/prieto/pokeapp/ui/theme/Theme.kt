package com.prieto.pokeapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightPokedexColorScheme = lightColorScheme(
    primary = PokedexRed,
    onPrimary = Color.White,
    primaryContainer = PokedexDarkRed,
    onPrimaryContainer = Color.White,
    secondary = PokedexScreenBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0369A1),
    tertiary = PokedexYellow,
    onTertiary = PokedexTextDark,
    background = PokedexBackground,
    onBackground = PokedexTextDark,
    surface = PokedexSurface,
    onSurface = PokedexTextDark,
    surfaceVariant = Color(0xFFEDF1F5),
    onSurfaceVariant = PokedexTextSecondary,
    error = Color(0xFFBA1A1A),
    onError = Color.White
)

private val DarkPokedexColorScheme = darkColorScheme(
    primary = Color(0xFFFFB4AB),
    onPrimary = Color(0xFF690005),
    primaryContainer = PokedexDarkRed,
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = PokedexScreenBlue,
    onSecondary = Color(0xFF00344F),
    tertiary = PokedexYellow,
    onTertiary = PokedexTextDark,
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF121316),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF2C2F33),
    onSurfaceVariant = Color(0xFFC4C6CF)
)

@Composable
fun PokeAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkPokedexColorScheme else LightPokedexColorScheme

    val view = LocalView.current
    if (!view.isInEditMode && view.context is Activity) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = PokedexRed.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
