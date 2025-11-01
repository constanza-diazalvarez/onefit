package com.example.onefit.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.onefit.api.RetrofitHelper
import com.example.onefit.model.EjercicioDto
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEjerciciosPantalla() {
    // Declaración y manejo del estado
    var listaEjercicios by remember { mutableStateOf<List<EjercicioDto>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // ----------------------------------------------------------------------
    // LÓGICA DE CARGA DE DATOS (Se ejecuta una sola vez al inicio)
    // ----------------------------------------------------------------------
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val ejerciciosBase = RetrofitHelper.api.obtenerEjercicios()

                listaEjercicios = ejerciciosBase.map { base ->
                    val imagenResponse = try {
                        RetrofitHelper.api.obtenerImagenEjercicio(base.id ?: "", "default")
                    } catch (e: Exception) {
                        null
                    }

                    base.copy(imageUrl = imagenResponse?.imageUrl)
                }
            } catch (e: Exception) {
                error = "Fallo al cargar los ejercicios: ${e.localizedMessage ?: "Error desconocido"}"
            } finally {
                cargando = false
            }
        }
    }


    // ----------------------------------------------------------------------
    // ESTRUCTURA DE PANTALLA (Usando Scaffold para la AppBar)
    // ----------------------------------------------------------------------
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Guía de Ejercicios", color = MaterialTheme.colorScheme.onPrimary) },
                // El color de la barra superior viene de `primary` en tu OneFitTheme
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        // El color del fondo de la pantalla viene de `background` en tu OneFitTheme
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        // ----------------------------------------------------------------------
        // Manejo de Estados de la UI (Carga, Error, Éxito)
        // ----------------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                cargando -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                error != null -> {
                    EstadoError(error = error, onRetry = {
                        cargando = true
                        error = null
                        scope.launch { /* Lógica de reintento de carga */ }
                    }, modifier = Modifier.align(Alignment.Center))
                }
                listaEjercicios.isEmpty() -> {
                    Text("No se encontraron ejercicios.", Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.onBackground)
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(listaEjercicios, key = { it.id ?: it.exerciseId ?: "" }) { ejercicio ->
                            EjercicioCard(ejercicio = ejercicio)
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------------------------
// Componente Reutilizable: Tarjeta de Ejercicio
// ----------------------------------------------------------------------
@Composable
fun EjercicioCard(ejercicio: EjercicioDto, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, shape = MaterialTheme.shapes.medium),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ IMAGEN DEL EJERCICIO
            AsyncImage(
                model = ejercicio.imageUrl,
                contentDescription = ejercicio.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            // ✅ INFORMACIÓN DEL EJERCICIO
            Column {
                Text(
                    text = ejercicio.name ?: "Sin nombre",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${ejercicio.bodyPart} • ${ejercicio.target}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ----------------------------------------------------------------------
// Componente Reutilizable: Estado de Error
// ----------------------------------------------------------------------

@Composable
fun EstadoError(error: String?, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error al cargar",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = error ?: "Detalle del error desconocido.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}