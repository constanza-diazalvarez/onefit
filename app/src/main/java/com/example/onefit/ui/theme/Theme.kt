package com.example.onefit.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val TemaOscuroColores = darkColorScheme(
    primary = Coral,
    onPrimary = Blanco,
    background = Gris,
    onBackground = Blanco,
    surface = Gris,
    onSurface = Blanco
)

private val TemaClaroColores = lightColorScheme(
    primary = Coral,
    onPrimary = Blanco,
    background = Blanco,
    onBackground = Gris,
    surface = Blanco,
    onSurface = Gris
)

@Composable
fun OneFitTheme(
    oscuro: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (oscuro) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        oscuro -> TemaOscuroColores
        else -> TemaClaroColores
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Pintamos la barra de estado (donde va la hora) de Coral
            window.statusBarColor = colorScheme.primary.toArgb()
            // Iconos blancos en la barra de estado
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !oscuro
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
