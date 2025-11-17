package com.example.onefit.model

import android.util.Log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RutinasRepository @Inject constructor(
    private val rutinaDao: RutinaDao,
    private val registroEntrenamientoDao: RegistroEntrenamientoDao
) {

    fun getAllRutinas(): Flow<List<Rutina>> {
        Log.d("REPO_DEBUG", "getAllRutinas() llamado")
        return rutinaDao.getAllRutinas()
    }

    fun getRutinaDetallada(idRutina: Int): Flow<RelacionRutinaConEjercicios> {
        return rutinaDao.getRutinaDetallada(idRutina)
    }

    suspend fun insertRutina(rutina: Rutina): Long {
        Log.d("REPO_DEBUG", "insertando rutina: ${rutina.nombre}")
        return rutinaDao.insertRutina(rutina)
    }

    suspend fun insertEjercicioRutina(ejercicio: EjercicioRutina) {
        rutinaDao.insertEjercicioRutina(ejercicio)
    }

    fun getAllRegistros(): Flow<List<RegistroEntrenamiento>> {
        return registroEntrenamientoDao.getHistorialEntrenamientos()
    }

    fun getRegistroDetallado(idRegistro: Int): Flow<RelacionRegistroEntrenamientoConSerie> {
        return registroEntrenamientoDao.getRegistroDetallado(idRegistro)
    }

    suspend fun insertRegistroCompleto(registro: RegistroEntrenamiento, series: List<RegistroSerieEntrenamiento>) {
        val idRegistro = registroEntrenamientoDao.insertRegistroEntrenamiento(registro)

        series.forEach { serie ->
            serie.registroSerieEntrenamientoId = idRegistro.toInt()
        }

        registroEntrenamientoDao.insertAllSeries(series)
    }
}