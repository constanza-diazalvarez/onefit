package com.example.onefit.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AgregarRutinaPantalla() {

    var ejercicio by remember { mutableStateOf("") }
    var series by remember { mutableStateOf("") }
    var repeticiones by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Registrar nueva rutina",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = ejercicio,
            onValueChange = { ejercicio = it },
            label = { Text("Ejercicio") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = series,
            onValueChange = { series = it.filter { c -> c.isDigit() } }, // deja solo números
            label = { Text("Series") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = repeticiones,
            onValueChange = { repeticiones = it.filter { c -> c.isDigit() } },
            label = { Text("Repeticiones por serie") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )


        OutlinedTextField(
            value = peso,
            onValueChange = { input ->
                val filtrado = input.filter { it.isDigit() || it == '.' }
                val conUnSoloPunto = if (filtrado.count { it == '.' } > 1) {
                    filtrado.dropLast(filtrado.count { it == '.' } - 1)
                } else filtrado
                peso = conUnSoloPunto
            },
            label = { Text("Peso (kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            )
        )

        Button(
            onClick = {
                println("Rutina: $ejercicio, $series, $repeticiones, $peso")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Guardar rutina")
        }
    }
}
