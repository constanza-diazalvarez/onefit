package com.example.onefit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onefit.model.EjercicioRutina
import com.example.onefit.model.RutinasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID // Para IDs únicos
import javax.inject.Inject

/**
 * Representa el estado de UN solo formulario de ejercicio en la lista.
 * Usamos un 'idLocal' para que Compose pueda identificar cada item.
 */
data class FormularioEjercicioEstado(
    val idLocal: UUID = UUID.randomUUID(),
    val nombre: String = "",
    val series: String = "4",
    val repeticiones: String = "10",
    val peso: String = "", //opcional, por eso String
    val errorNombre: String? = null,
    val errorSeries: String? = null,
    val errorReps: String? = null
)

@HiltViewModel
class AgregarEjerciciosViewModel @Inject constructor(
    private val repository: RutinasRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val rutinaId: Int = checkNotNull(savedStateHandle.get("rutinaId"))

    private val _listaFormularios = MutableStateFlow(listOf<FormularioEjercicioEstado>())
    val listaFormularios = _listaFormularios.asStateFlow()

    private val _navegarALista = MutableStateFlow(false)
    val navegarALista = _navegarALista.asStateFlow()

    init {
        agregarNuevoEjercicio()
    }

    fun agregarNuevoEjercicio() {
        _listaFormularios.update { listaActual ->
            listaActual + FormularioEjercicioEstado()
        }
    }

    fun eliminarEjercicio(idLocal: UUID) {
        _listaFormularios.update { listaActual ->
            listaActual.filterNot { it.idLocal == idLocal }
        }
    }


    fun onNombreChange(idLocal: UUID, valor: String) {
        actualizarFormulario(idLocal) { it.copy(nombre = valor, errorNombre = null) }
    }

    fun onSeriesChange(idLocal: UUID, valor: String) {
        actualizarFormulario(idLocal) { it.copy(series = valor, errorSeries = null) }
    }

    fun onRepeticionesChange(idLocal: UUID, valor: String) {
        actualizarFormulario(idLocal) { it.copy(repeticiones = valor, errorReps = null) }
    }

    fun onPesoChange(idLocal: UUID, valor: String) {
        actualizarFormulario(idLocal) { it.copy(peso = valor) }
    }

    private fun actualizarFormulario(
        idLocal: UUID,
        transform: (FormularioEjercicioEstado) -> FormularioEjercicioEstado
    ) {
        _listaFormularios.update { listaActual ->
            listaActual.map { formulario ->
                if (formulario.idLocal == idLocal) {
                    transform(formulario)
                } else {
                    formulario
                }
            }
        }
    }

    fun onGuardarRutinaClick() {
        val formularios = _listaFormularios.value
        var esValidoGlobal = true

        // 1. Validación (¡Rúbrica!)
        val formulariosValidados = formularios.map { form ->
            val errorNombre = if (form.nombre.isBlank()) "Obligatorio" else null
            val errorSeries = if (form.series.toIntOrNull() == null) "Inválido" else null
            val errorReps = if (form.repeticiones.toIntOrNull() == null) "Inválido" else null

            if (errorNombre != null || errorSeries != null || errorReps != null) {
                esValidoGlobal = false
            }

            form.copy(
                errorNombre = errorNombre,
                errorSeries = errorSeries,
                errorReps = errorReps
            )
        }

        _listaFormularios.value = formulariosValidados

        if (!esValidoGlobal) return

        val listaEjerciciosParaGuardar = formularios.map { form ->
            EjercicioRutina(
                rutinaId = rutinaId,
                nombreEjercicio = form.nombre,
                series = form.series.toInt(),
                repeticiones = form.repeticiones.toInt(),
                peso = form.peso.toDoubleOrNull()
            )
        }

        viewModelScope.launch {
            repository.insertListaEjercicios(listaEjerciciosParaGuardar)
            _navegarALista.value = true
        }
    }
}