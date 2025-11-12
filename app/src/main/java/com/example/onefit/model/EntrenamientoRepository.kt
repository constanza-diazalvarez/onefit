package com.example.onefit.model

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntrenamientoRepository @Inject constructor(
    private val rutinaDao: RutinaDao,
    private val registroEntrenamientoDao: RegistroEntrenamientoDao
) {

    fun getAllRutinas(): Flow<List<Rutina>> {
        return rutinaDao.getAllRutinas()
    }

    fun getRutinaDetallada(idRutina: Int): Flow<RelacionRutinaConEjercicios> {
        return rutinaDao.getRutinaDetallada(idRutina)
    }

    suspend fun insertRutina(rutina: Rutina) {
        rutinaDao.insertRutina(rutina)
    }

    suspend fun insertEjercicioRutina(ejercicio: EjercicioRutina) {
        rutinaDao.insertEjercicioRutina(ejercicio)
    }

    fun getAllRegistros(): Flow<List<RegistroEntrenamiento>> {
        // Usamos el nombre de TU método (no el mío)
        return registroEntrenamientoDao.getHistorialEntrenamientos() // <-- CORREGIDO
    }

    fun getRegistroDetallado(idRegistro: Int): Flow<RelacionRegistroEntrenamientoConSerie> {
        return registroEntrenamientoDao.getRegistroDetallado(idRegistro)
    }

    suspend fun insertRegistroCompleto(registro: RegistroEntrenamiento, series: List<RegistroSerieEntrenamiento>) {
        val idRegistro = registroEntrenamientoDao.insertRegistroEntrenamiento(registro)

        // Ahora, asignamos ese ID a cada una of las series
        series.forEach { serie ->
            serie.registroSerieEntrenamientoId = idRegistro.toInt()
        }

        registroEntrenamientoDao.insertAllSeries(series)
    }
}