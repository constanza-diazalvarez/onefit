package com.example.onefit.ui.pantallas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.onefit.model.Rutina
import com.example.onefit.viewmodel.ListarRutinasViewModel

/**
 * Pantalla principal que muestra la lista de rutinas.
 * Es una función @Composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarRutinasPantalla(
    // Pedimos el ViewModel. Hilt se lo inyectará automáticamente
    viewModel: ListarRutinasViewModel = hiltViewModel()
) {
    // Observamos la variable 'todasLasRutinas' del ViewModel.
    // 'observeAsState' convierte el LiveData en un "Estado" de Compose.
    val rutinasList by viewModel.todasLasRutinas.observeAsState(initial = emptyList())

    // Scaffold es la estructura básica de Material Design
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Rutinas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: Aquí irá la navegación para
                    // ir a la pantalla de "Crear Rutina" (OF-1)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Rutina")
            }
        }
    ) { paddingValues ->

        // Comprobamos si la lista está vacía
        if (rutinasList.isEmpty()) {
            // Mostramos un mensaje centrado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no tienes rutinas. ¡Presiona el botón '+' para crear la primera!",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        } else {
            // Mostramos la lista de rutinas
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 16.dp)
            ) {
                items(rutinasList) { rutina ->
                    RutinaItem(rutina = rutina)
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

/**
 * Un Composable que dibuja una sola fila (un item) de la lista.
 */
@Composable
fun RutinaItem(rutina: Rutina) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = rutina.nombre,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        if (rutina.descripcion?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = rutina.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}