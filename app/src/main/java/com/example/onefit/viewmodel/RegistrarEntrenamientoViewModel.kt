package com.example.onefit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onefit.model.RegistroEntrenamiento
import com.example.onefit.model.RegistroSerieEntrenamiento
import com.example.onefit.model.RutinasRepository
import com.example.onefit.utils.Vibrador
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EstadoSesionEjercicio(
    val nombreEjercicio: String,
    val series: List<EstadoSerieInput>
)

data class EstadoSerieInput(
    val numeroSerie: Int,
    val repsInput: String = "",
    val pesoInput: String = "",
    val estaCompletada: Boolean = false
)

@HiltViewModel
class RegistrarEntrenamientoViewModel @Inject constructor(
    private val repository: RutinasRepository,
    savedStateHandle: SavedStateHandle,
    private val vibrador: Vibrador
) : ViewModel() {

    private val rutinaId: Int = checkNotNull(savedStateHandle.get("rutinaId"))

    private val _ejerciciosSesion = MutableStateFlow<List<EstadoSesionEjercicio>>(emptyList())
    val ejerciciosSesion = _ejerciciosSesion.asStateFlow()

    private val _nombreRutina = MutableStateFlow("")
    val nombreRutina = _nombreRutina.asStateFlow()

    private val _navegarAlHistorial = MutableStateFlow(false)
    val navegarAlHistorial = _navegarAlHistorial.asStateFlow()

    private val _mostrarDialogoExito = MutableStateFlow(false)
    val mostrarDialogoExito = _mostrarDialogoExito.asStateFlow()

    private val _navegarAlInicio = MutableStateFlow(false)
    val navegarAlInicio = _navegarAlInicio.asStateFlow()

    init {
        cargarRutinaParaEntrenar()
    }

    private fun cargarRutinaParaEntrenar() {
        viewModelScope.launch {
            repository.getRutinaDetallada(rutinaId).collect { detalle ->
                _nombreRutina.value = detalle.rutina.nombre

                val listaInicial = detalle.ejercicios.map { ejercicioPlan ->

                    val seriesVacias = List(ejercicioPlan.series) { index ->
                        EstadoSerieInput(
                            numeroSerie = index + 1,
                            repsInput = ejercicioPlan.repeticiones.toString(),
                            pesoInput = ejercicioPlan.peso?.toString() ?: ""
                        )
                    }

                    EstadoSesionEjercicio(
                        nombreEjercicio = ejercicioPlan.nombreEjercicio,
                        series = seriesVacias
                    )
                }
                _ejerciciosSesion.value = listaInicial
            }
        }
    }
    fun onSerieUpdate(ejercicioIndex: Int, serieIndex: Int, reps: String, peso: String, completado: Boolean) {
        _ejerciciosSesion.update { lista ->
            val nuevaLista = lista.toMutableList()
            val ejercicio = nuevaLista[ejercicioIndex]
            val nuevasSeries = ejercicio.series.toMutableList()

            nuevasSeries[serieIndex] = nuevasSeries[serieIndex].copy(
                repsInput = reps,
                pesoInput = peso,
                estaCompletada = completado
            )

            nuevaLista[ejercicioIndex] = ejercicio.copy(series = nuevasSeries)
            nuevaLista
        }
    }

    fun finalizarEntrenamiento() {
        viewModelScope.launch {
            val nuevoRegistro = RegistroEntrenamiento(
                nombreRutina = _nombreRutina.value,
                date = System.currentTimeMillis()
            )

            val seriesParaGuardar = mutableListOf<RegistroSerieEntrenamiento>()

            _ejerciciosSesion.value.forEach { ejercicio ->
                ejercicio.series.forEach { serie ->
                    if (serie.repsInput.isNotBlank()) {
                        seriesParaGuardar.add(
                            RegistroSerieEntrenamiento(
                                registroSerieEntrenamientoId = 0,
                                nombreEjercicio = ejercicio.nombreEjercicio,
                                numeroSerie = serie.numeroSerie,
                                repeticiones = serie.repsInput.toIntOrNull() ?: 0,
                                peso = serie.pesoInput.toDoubleOrNull()
                            )
                        )
                    }
                }
            }

            repository.insertRegistroCompleto(nuevoRegistro, seriesParaGuardar)
            vibrador.vibrar(500)

            _mostrarDialogoExito.value = true
            //_navegarAlHistorial.value = true
        }
    }

    fun cerrarDialogoYNavegar() {
        _mostrarDialogoExito.value = false
        _navegarAlHistorial.value = true
    }
}