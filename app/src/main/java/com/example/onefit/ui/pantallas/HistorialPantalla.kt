package com.example.onefit.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.onefit.model.RegistroEntrenamiento
import com.example.onefit.viewmodel.HistorialViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialPantalla(
    navController: NavController,
    viewModel: HistorialViewModel = hiltViewModel()
) {
    val mesSeleccionado by viewModel.mesSeleccionado.collectAsState()
    val entrenamientosMap by viewModel.entrenamientosPorFecha.collectAsState()

    // --- NUEVO: Observamos estadísticas ---
    val estadisticas by viewModel.estadisticas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("inicio") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- CONTROL DE MES ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.cambiarMes(false) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Anterior")
                }

                Text(
                    text = "${mesSeleccionado.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).uppercase()} ${mesSeleccionado.year}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = { viewModel.cambiarMes(true) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "Siguiente")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- CALENDARIO ---
            CalendarioGrid(
                year = mesSeleccionado.year,
                month = mesSeleccionado.monthValue,
                entrenamientos = entrenamientosMap,
                onDiaClick = { idRegistro ->
                    navController.navigate("detalle_historial/$idRegistro")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- NUEVO: SECCIÓN DE RESUMEN MENSUAL ---
            Text(
                text = "Resumen del Mes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tarjeta 1: Total Entrenamientos
                TarjetaEstadistica(
                    titulo = "Total Sesiones",
                    valor = estadisticas.totalEntrenamientos.toString(),
                    icono = Icons.Default.EmojiEvents,
                    modifier = Modifier.weight(1f),
                    colorIcono = MaterialTheme.colorScheme.tertiary
                )

                // Tarjeta 2: Rutina Favorita
                TarjetaEstadistica(
                    titulo = "Más Frecuente",
                    valor = estadisticas.rutinaMasFrecuente,
                    icono = Icons.Default.FitnessCenter,
                    modifier = Modifier.weight(1f),
                    colorIcono = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun TarjetaEstadistica(
    titulo: String,
    valor: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    colorIcono: Color
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CalendarioGrid(
    year: Int,
    month: Int,
    entrenamientos: Map<LocalDate, List<RegistroEntrenamiento>>,
    onDiaClick: (Int) -> Unit
) {
    val primerDiaDelMes = LocalDate.of(year, month, 1)
    val diasEnMes = primerDiaDelMes.lengthOfMonth()
    val diaSemanaInicio = primerDiaDelMes.dayOfWeek.value
    val celdasTotales = (1 until diaSemanaInicio).map { -1 } + (1..diasEnMes).map { it }

    Column {
        Row(Modifier.fillMaxWidth()) {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { dia ->
                Text(
                    text = dia,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(celdasTotales) { dia ->
                if (dia == -1) {
                    Spacer(modifier = Modifier.size(40.dp))
                } else {
                    val fechaActual = LocalDate.of(year, month, dia)
                    val entrenamientosDelDia = entrenamientos[fechaActual]
                    val hayEntreno = !entrenamientosDelDia.isNullOrEmpty()

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(
                                if (hayEntreno) MaterialTheme.colorScheme.tertiary else Color.Transparent
                            )
                            .clickable(enabled = hayEntreno) {
                                entrenamientosDelDia?.firstOrNull()?.let {
                                    onDiaClick(it.id)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dia.toString(),
                            color = if (hayEntreno) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (hayEntreno) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}