package com.example.onefit.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.onefit.viewmodel.AgregarEjerciciosViewModel
import com.example.onefit.viewmodel.FormularioEjercicioEstado
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarEjerciciosPantalla(
    navController: NavController,
    viewModel: AgregarEjerciciosViewModel = hiltViewModel()
) {
    // Observamos el estado principal (la LISTA de formularios)
    val listaFormularios by viewModel.listaFormularios.collectAsState()
    val navegarALista by viewModel.navegarALista.collectAsState()

    // Efecto para navegar a la lista principal cuando se guarda
    LaunchedEffect(navegarALista) {
        if (navegarALista) {
            // Volvemos hasta la lista de rutinas, limpiando el historial
            navController.navigate("lista_rutinas") {
                popUpTo("lista_rutinas") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Ejercicios (Paso 2)") },
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

        // Usamos LazyColumn para que la lista sea "scrollable"
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- 1. RENDERIZAMOS LA LISTA DE FORMULARIOS ---
            items(listaFormularios, key = { it.idLocal }) { formulario ->
                FormularioEjercicioItem(
                    estado = formulario,
                    onNombreChange = { viewModel.onNombreChange(formulario.idLocal, it) },
                    onSeriesChange = { viewModel.onSeriesChange(formulario.idLocal, it) },
                    onRepeticionesChange = { viewModel.onRepeticionesChange(formulario.idLocal, it) },
                    onPesoChange = { viewModel.onPesoChange(formulario.idLocal, it) },
                    onEliminar = { viewModel.eliminarEjercicio(formulario.idLocal) },
                    // Solo mostramos el botón de eliminar si hay más de uno
                    mostrarBotonEliminar = listaFormularios.size > 1
                )
            }

            // --- 2. BOTONES DE ACCIÓN ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Botón para agregar otro formulario a la lista
                OutlinedButton(
                    onClick = { viewModel.agregarNuevoEjercicio() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar otro ejercicio")
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Botón final para guardar todo
                Button(
                    onClick = { viewModel.onGuardarRutinaClick() },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("GUARDAR RUTINA")
                }
            }
        }
    }
}


/**
 * Un Composable reutilizable que representa UN SOLO formulario
 * de ejercicio en la lista dinámica.
 */
@Composable
fun FormularioEjercicioItem(
    estado: FormularioEjercicioEstado,
    onNombreChange: (String) -> Unit,
    onSeriesChange: (String) -> Unit,
    onRepeticionesChange: (String) -> Unit,
    onPesoChange: (String) -> Unit,
    onEliminar: () -> Unit,
    mostrarBotonEliminar: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- Fila 1: Nombre y Botón Eliminar ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = estado.nombre,
                    onValueChange = onNombreChange,
                    label = { Text("Nombre Ejercicio") },
                    modifier = Modifier.weight(1f),
                    isError = estado.errorNombre != null,
                    singleLine = true
                )
                if (mostrarBotonEliminar) {
                    IconButton(onClick = onEliminar) {
                        Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            if (estado.errorNombre != null) {
                Text(estado.errorNombre, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // --- Fila 2: Series, Reps, Peso ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // --- Series ---
                OutlinedTextField(
                    value = estado.series,
                    onValueChange = onSeriesChange,
                    label = { Text("Series") },
                    isError = estado.errorSeries != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                // --- Repeticiones ---
                OutlinedTextField(
                    value = estado.repeticiones,
                    onValueChange = onRepeticionesChange,
                    label = { Text("Reps") },
                    isError = estado.errorReps != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                // --- Peso (Opcional) ---
                OutlinedTextField(
                    value = estado.peso,
                    onValueChange = onPesoChange,
                    label = { Text("Peso (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}