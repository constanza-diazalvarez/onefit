package com.example.onefit.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import com.example.onefit.model.EjercicioRutina // Importa tu entidad
import com.example.onefit.viewmodel.RutinaDetalleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaDetallePantalla(
    navController: NavController, // El controlador para poder "volver"
    viewModel: RutinaDetalleViewModel = hiltViewModel()
) {
    // Observamos el StateFlow del ViewModel.
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
            FloatingActionButton(
                onClick = {
                    // TODO: Navegar a la pantalla 'AgregarEjercicioPantalla'
                    // Pasándole el ID: rutinaDetalle?.rutina?.id
                }
            ) {
                Icon(Icons.Default.Add, "Agregar Ejercicio")
            }
        }
    ) { paddingValues ->

        // Si rutinaDetalle no es nulo, mostramos la lista
        rutinaDetalle?.let { detalle ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Mostramos la descripción (si existe)
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

                // Título para la lista de ejercicios
                item {
                    Text(
                        text = "Ejercicios",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Mostramos los ejercicios (si no hay, la lista estará vacía)
                if (detalle.ejercicios.isEmpty()) {
                    item {
                        Text(
                            "Aún no hay ejercicios. Presiona '+' para agregar el primero.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    // Iteramos sobre la lista de ejercicios (los "hijos")
                    items(detalle.ejercicios) { ejercicio ->
                        EjercicioItem(
                            nombre = ejercicio.nombreEjercicio,
                            series = ejercicio.series,
                            reps = ejercicio.repeticiones
                        )
                    }
                }

                // Botón "Finalizar" al final de la lista
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            // Al finalizar, volvemos a la lista principal
                            navController.popBackStack("lista_rutinas", inclusive = false)
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("FINALIZAR RUTINA")
                    }
                }
            }
        } ?: run {
            // Muestra un "Cargando..."
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

/**
 * Un Composable para mostrar un solo ejercicio de la lista
 */
@Composable
fun EjercicioItem(nombre: String, series: Int, reps: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                text = "$series x $reps", // Ej: "4 x 10"
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}