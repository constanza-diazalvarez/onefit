package com.example.onefit.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.onefit.viewmodel.EstadoSerieInput
import com.example.onefit.viewmodel.RegistrarEntrenamientoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarEntrenamientoPantalla(
    navController: NavController,
    viewModel: RegistrarEntrenamientoViewModel = hiltViewModel()
) {
    val ejercicios by viewModel.ejerciciosSesion.collectAsState()
    val nombreRutina by viewModel.nombreRutina.collectAsState()
    val navegarAlHistorial by viewModel.navegarAlHistorial.collectAsState()

    LaunchedEffect(navegarAlHistorial) {
        if (navegarAlHistorial) {
            navController.popBackStack("lista_rutinas", inclusive = false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Entrenando: $nombreRutina") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { viewModel.finalizarEntrenamiento() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text("Finalizar Entrenamiento")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            itemsIndexed(ejercicios) { indexEjercicio, ejercicio ->
                TarjetaEjercicioSesion(
                    nombre = ejercicio.nombreEjercicio,
                    series = ejercicio.series,
                    onSerieChange = { indexSerie, reps, peso, check ->
                        viewModel.onSerieUpdate(indexEjercicio, indexSerie, reps, peso, check)
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaEjercicioSesion(
    nombre: String,
    series: List<EstadoSerieInput>,
    onSerieChange: (Int, String, String, Boolean) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Serie", Modifier.weight(0.8f), fontWeight = FontWeight.Bold)
                Text("Kg", Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Reps", Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(0.5f)) // Espacio para el check
            }
            Spacer(modifier = Modifier.height(8.dp))

            series.forEachIndexed { index, serie ->
                FilaSerieInput(
                    serie = serie,
                    onChange = { r, p, c -> onSerieChange(index, r, p, c) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun FilaSerieInput(
    serie: EstadoSerieInput,
    onChange: (String, String, Boolean) -> Unit
) {
    val backgroundColor = if (serie.estaCompletada)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = MaterialTheme.shapes.small)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "${serie.numeroSerie}",
            modifier = Modifier.weight(0.8f).padding(start = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = serie.pesoInput,
            onValueChange = { onChange(serie.repsInput, it, serie.estaCompletada) },
            modifier = Modifier.weight(1f).padding(end = 4.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        OutlinedTextField(
            value = serie.repsInput,
            onValueChange = { onChange(it, serie.pesoInput, serie.estaCompletada) },
            modifier = Modifier.weight(1f).padding(end = 4.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        IconButton(
            onClick = { onChange(serie.repsInput, serie.pesoInput, !serie.estaCompletada) },
            modifier = Modifier.weight(0.5f)
        ) {
            Icon(
                imageVector = if (serie.estaCompletada) Icons.Default.Check else Icons.Default.Close,
                contentDescription = "Completar",
                tint = if (serie.estaCompletada) MaterialTheme.colorScheme.tertiary else Color.Gray
            )
        }
    }
}