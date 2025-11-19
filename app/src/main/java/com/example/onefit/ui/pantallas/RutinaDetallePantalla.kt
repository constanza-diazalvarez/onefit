package com.example.onefit.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.onefit.viewmodel.RutinaDetalleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaDetallePantalla(
    navController: NavController,
    viewModel: RutinaDetalleViewModel = hiltViewModel()
) {
    val rutinaDetalle by viewModel.rutinaDetalle.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(rutinaDetalle?.rutina?.nombre ?: "Cargando...")
                },
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
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    rutinaDetalle?.let {
                        navController.navigate("registrar_entrenamiento/${it.rutina.id}")
                    }
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(Icons.Default.PlayArrow, "Comenzar")
                Spacer(Modifier.width(8.dp))
                Text("Comenza Entrenamiento")
            }
        }
    ) { paddingValues ->

        rutinaDetalle?.let { detalle ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (detalle.rutina.descripcion?.isNotEmpty() == true) {
                    item {
                        Text(
                            text = detalle.rutina.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                item {
                    Text(
                        text = "Ejercicios de la Rutina",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (detalle.ejercicios.isEmpty()) {
                    item {
                        Text(
                            "Esta rutina no tiene ejercicios.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(detalle.ejercicios) { ejercicio ->
                        EjercicioItemDetalle(
                            nombre = ejercicio.nombreEjercicio,
                            series = ejercicio.series,
                            reps = ejercicio.repeticiones
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        } ?: run {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun EjercicioItemDetalle(nombre: String, series: Int, reps: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$series x $reps",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}