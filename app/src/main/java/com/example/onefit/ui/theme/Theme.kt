package com.example.onefit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable


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
    content: @Composable () -> Unit
) {
    val colores = if (oscuro) TemaOscuroColores else TemaClaroColores

    MaterialTheme(
        colorScheme = colores,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
