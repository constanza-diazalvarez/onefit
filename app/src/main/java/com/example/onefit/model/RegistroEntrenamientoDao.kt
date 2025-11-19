package com.example.onefit.model

import androidx.room.*
import com.example.onefit.model.RegistroEntrenamiento
import com.example.onefit.model.RelacionRegistroEntrenamientoConSerie
import com.example.onefit.model.RegistroSerieEntrenamiento
import kotlinx.coroutines.flow.Flow

/**
 * DAO para toda la gestión del "Historial" (Registros de Entrenamiento y sus Series).
 */
@Dao
interface RegistroEntrenamientoDao {
    /*se utiliza suspend para operaciones puntuales (insert, update, delete)
    *flow es para operaciones reactivas (en vivo)*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistroEntrenamiento(registro: RegistroEntrenamiento): Long

    @Query("SELECT * FROM registro_entrenamiento ORDER BY fecha DESC")
    fun getHistorialEntrenamientos(): Flow<List<RegistroEntrenamiento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSerie(serie: RegistroSerieEntrenamiento)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSeries(series: List<RegistroSerieEntrenamiento>)

    @Transaction
    @Query("SELECT * FROM registro_entrenamiento WHERE id = :idRegistro")
    fun getRegistroDetallado(idRegistro: Int): Flow<RelacionRegistroEntrenamientoConSerie>

    @Query("SELECT * FROM registro_serie_entrenamiento WHERE nombre_ejercicio = :nombreEjercicio ORDER BY id ASC")
    fun getProgresoEjercicio(nombreEjercicio: String): Flow<List<RegistroSerieEntrenamiento>>
}

