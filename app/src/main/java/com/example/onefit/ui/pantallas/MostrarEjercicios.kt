package com.example.onefit.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.onefit.model.EjercicioDto
import com.example.onefit.api.RetrofitHelper
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch

@Composable
fun ListaEjercicios() {
    var listaEjercicios by remember { mutableStateOf<List<EjercicioDto>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val respuesta = RetrofitHelper.api.obtenerEjercicios()
            listaEjercicios = respuesta
        } catch (e: Exception) {
            error = e.localizedMessage ?: "Error desconocido"
        } finally {
            cargando = false
        }
    }

    if (cargando) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $error")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaEjercicios) { ejercicio ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ejercicio.imageUrl?.let { url ->
                            Image(
                                painter = rememberImagePainter(url),
                                contentDescription = ejercicio.name,
                                modifier = Modifier
                                    .size(64.dp)
                            )
                        }
                        Column {
                            Text(text = ejercicio.name ?: "Sin nombre", style = MaterialTheme.typography.titleMedium)
                            Text(text = "ID: ${ejercicio.exerciseId}")
                        }
                    }
                }
            }
        }
    }
}