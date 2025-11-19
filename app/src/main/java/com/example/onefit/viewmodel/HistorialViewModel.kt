package com.example.onefit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onefit.model.RegistroEntrenamiento
import com.example.onefit.model.RutinasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

data class EstadisticasMes(
    val totalEntrenamientos: Int = 0,
    val rutinaMasFrecuente: String = "-",
    val rachaActual: Int = 0
)

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val repository: RutinasRepository
) : ViewModel() {

    private val _mesSeleccionado = MutableStateFlow(YearMonth.now())
    val mesSeleccionado = _mesSeleccionado.asStateFlow()

    private val _entrenamientosPorFecha = MutableStateFlow<Map<LocalDate, List<RegistroEntrenamiento>>>(emptyMap())
    val entrenamientosPorFecha = _entrenamientosPorFecha.asStateFlow()

    private val _estadisticas = MutableStateFlow(EstadisticasMes())
    val estadisticas = _estadisticas.asStateFlow()

    private var listaCompletaCache: List<RegistroEntrenamiento> = emptyList()

    init {
        cargarHistorial()
    }

    private fun cargarHistorial() {
        viewModelScope.launch {
            repository.getAllRegistros().collect { lista ->
                listaCompletaCache = lista

                // 1. Agrupar para el calendario
                val mapa = lista.groupBy { registro ->
                    Instant.ofEpochMilli(registro.date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
                _entrenamientosPorFecha.value = mapa

                calcularEstadisticas(YearMonth.now())
            }
        }
    }

    fun cambiarMes(avanzar: Boolean) {
        val nuevoMes = if (avanzar) {
            _mesSeleccionado.value.plusMonths(1)
        } else {
            _mesSeleccionado.value.minusMonths(1)
        }
        _mesSeleccionado.value = nuevoMes

        calcularEstadisticas(nuevoMes)
    }

    private fun calcularEstadisticas(mes: YearMonth) {
        val entrenamientosDelMes = listaCompletaCache.filter {
            val fecha = Instant.ofEpochMilli(it.date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            YearMonth.from(fecha) == mes
        }

        val total = entrenamientosDelMes.size
        val favorita = if (total > 0) {
            entrenamientosDelMes
                .groupBy { it.nombreRutina }
                .maxByOrNull { it.value.size }
                ?.key ?: "-"
        } else {
            "-"
        }

        _estadisticas.value = EstadisticasMes(
            totalEntrenamientos = total,
            rutinaMasFrecuente = favorita
        )
    }
}