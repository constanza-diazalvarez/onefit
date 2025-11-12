package com.example.onefit.model

import androidx.room.*
import com.example.onefit.model.EjercicioRutina
import com.example.onefit.model.Rutina
import com.example.onefit.model.RelacionRutinaConEjercicios
import kotlinx.coroutines.flow.Flow

/**
 * DAO para toda la gestión de las "Plantillas" (Rutinas y sus Ejercicios).
 */
@Dao
interface RutinaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRutina(rutina: Rutina)

    @Update
    suspend fun updateRutina(rutina: Rutina)

    @Delete
    suspend fun deleteRutina(rutina: Rutina)

    @Query("SELECT * FROM rutina ORDER BY nombre_rutina ASC")
    fun getAllRutinas(): Flow<List<Rutina>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEjercicioRutina(ejercicio: EjercicioRutina)

    @Update
    suspend fun updateEjercicioRutina(ejercicio: EjercicioRutina)

    @Delete
    suspend fun deleteEjercicioRutina(ejercicio: EjercicioRutina)

    @Transaction
    @Query("SELECT * FROM rutina WHERE id = :idRutina")
    fun getRutinaDetallada(idRutina: Int): Flow<RelacionRutinaConEjercicios>
}

