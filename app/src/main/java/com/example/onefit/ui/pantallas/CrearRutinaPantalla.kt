package com.example.onefit.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
// ¡CORRECCIÓN 1: IMPORTAMOS EL ICONO NORMAL!
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.onefit.viewmodel.CrearRutinaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRutinaPantalla(
    navController: NavController,
    viewModel: CrearRutinaViewModel = hiltViewModel()
) {

    val nombre by viewModel.nombre.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()
    val errorNombre by viewModel.errorNombre.collectAsState() // Esto es String?
    val navegarSiguiente by viewModel.navegarSiguiente.collectAsState()

    LaunchedEffect(navegarSiguiente) {
        navegarSiguiente?.let { nuevoId ->
            navController.navigate("detalle_rutina/${nuevoId.toInt()}") {
                popUpTo("lista_rutinas")
            }
            viewModel.onNavegacionCompletada()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nueva Rutina") },
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
                onClick = { viewModel.onSiguienteClick() }
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Siguiente")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val errorActual = errorNombre

            OutlinedTextField(
                value = nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre de la Rutina *") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorActual != null,
                singleLine = true,
                trailingIcon = {
                    if (errorActual != null) {
                        Icon(Icons.Default.Error, "Error", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )

            if (errorActual != null) {
                Text(
                    text = errorActual,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = { viewModel.onDescripcionChange(it) },
                label = { Text("Descripción (Opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.onSiguienteClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("SIGUIENTE (AGREGAR EJERCICIOS)", fontSize = 16.sp)
            }
        }
    }
}