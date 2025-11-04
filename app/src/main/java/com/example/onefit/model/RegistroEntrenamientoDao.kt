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

    // --- Funciones para RegistroEntrenamiento (OF-5, OF-6) ---

    /**
     * Inserta la cabecera de un entrenamiento (ej: "Entrenamiento del Lunes")
     * Devuelve el ID (Long) que se generó, para que podamos usarlo
     * para asociar las series.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistroEntrenamiento(registro: RegistroEntrenamiento): Long

    /**
     * Obtiene la lista de todos los entrenamientos guardados,
     * para mostrarlos en el historial (OF-6).
     */
    @Query("SELECT * FROM registro_entrenamiento ORDER BY fecha DESC")
    fun getHistorialEntrenamientos(): Flow<List<RegistroEntrenamiento>>

    // --- Funciones para RegistroSerieEntrenamiento (OF-5) ---

    /**
     * Inserta una única serie que pertenece a un registro de entrenamiento.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSerie(serie: RegistroSerieEntrenamiento)

    /**
     * Inserta una lista de series de golpe (más eficiente).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSeries(series: List<RegistroSerieEntrenamiento>)


    // --- Función de Composición (para ver detalles del historial) ---

    /**
     * Trae el Registro (padre) Y su lista de series (hijos), todo de una vez.
     * Se usa para ver el detalle de un entrenamiento del historial.
     */
    @Transaction
    @Query("SELECT * FROM registro_entrenamiento WHERE id = :idRegistro")
    fun getRegistroDetallado(idRegistro: Int): Flow<RelacionRegistroEntrenamientoConSerie>

    // --- Función para Gráfica (OF-7) ---

    /**
     * Obtiene todas las series registradas para UN ejercicio específico,
     * ordenadas por fecha (implícita por el ID auto-incremental).
     * Esto es lo que usaremos para generar los gráficos de progreso.
     */
    @Query("SELECT * FROM registro_serie_entrenamiento WHERE nombre_ejercicio = :nombreEjercicio ORDER BY id ASC")
    fun getProgresoEjercicio(nombreEjercicio: String): Flow<List<RegistroSerieEntrenamiento>>
}

