package com.example.onefit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
// Importa tus pantallas
import com.example.onefit.ui.pantallas.CrearRutinaPantalla
import com.example.onefit.ui.pantallas.ListarRutinasPantalla
import com.example.onefit.ui.theme.OneFitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("API_KEY_DEBUG", "API Key usada: ${BuildConfig.RAPID_API_KEY}")
        setContent {
            OneFitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "crear_rutina" //pagina inicial
                    ) {
                        composable(route = "lista_rutinas") {
                            ListarRutinasPantalla(
                                navController = navController
                            )
                        }

                        composable(route = "crear_rutina") {
                            CrearRutinaPantalla(
                                navController = navController
                            )
                        }

                        //la pantalla que recibe el id de la rutina recién creada
                        /*
                        composable(
                            route = "detalle_rutina/{rutinaId}",
                            arguments = listOf(navArgument("rutinaId") {
                                type = NavType.IntType
                            })
                        ) {
                            RutinaDetallePantalla(
                                navController = navController
                                // Hilt se encarga del ViewModel
                            )
                        }*/
                    }
                }
            }
        }
    }
}