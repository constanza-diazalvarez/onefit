package com.example.onefit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.onefit.ui.pantallas.AgregarRutinaPantalla
import com.example.onefit.ui.pantallas.ListaEjerciciosPantalla
import com.example.onefit.ui.theme.OneFitTheme
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("API_KEY_DEBUG", "API Key usada: ${BuildConfig.RAPID_API_KEY}")

        setContent {
            OneFitTheme {
                //AgregarRutinaPantalla()

                ListaEjerciciosPantalla()

            }
        }
    }
}