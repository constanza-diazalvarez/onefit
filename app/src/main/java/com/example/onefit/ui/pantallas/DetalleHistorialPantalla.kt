package com.example.onefit.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.onefit.viewmodel.DetalleHistorialViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleHistorialPantalla(
    navController: NavController,
    viewModel: DetalleHistorialViewModel = hiltViewModel()
) {
    val detalle by viewModel.detalle.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen Entrenamiento") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
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

        detalle?.let { data ->
            val fecha = Instant.ofEpochMilli(data.registro.date)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))

            val ejerciciosAgrupados = data.series.groupBy { it.nombreEjercicio }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column {
                        Text(
                            text = data.registro.nombreRutina,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = fecha,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                    }
                }

                items(ejerciciosAgrupados.keys.toList()) { nombreEjercicio ->

                    val seriesDeEsteEjercicio = ejerciciosAgrupados[nombreEjercicio] ?: emptyList()

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = nombreEjercicio,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text("Serie", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                                Text("Reps", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Center)
                                Text("Peso", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.End)
                            }

                            Divider(modifier = Modifier.padding(vertical = 4.dp))

                            seriesDeEsteEjercicio.forEach { serie ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "#${serie.numeroSerie}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f),
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "${serie.repeticiones}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Center
                                    )

                                    val textoPeso = if ((serie.peso ?: 0.0) > 0.0) "${serie.peso} kg" else "-"
                                    Text(
                                        text = textoPeso,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}