package com.example.onefit.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.onefit.model.EjercicioRutina
import com.example.onefit.model.RegistroEntrenamiento
import com.example.onefit.model.RegistroSerieEntrenamiento
import com.example.onefit.model.Rutina

@Database(
    entities = [
        Rutina::class,
        EjercicioRutina::class,
        RegistroEntrenamiento::class,
        RegistroSerieEntrenamiento::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    // Room implementará estas funciones abstractas por nosotros
    abstract fun rutinaDao(): RutinaDao
    abstract fun registroEntrenamientoDao(): RegistroEntrenamientoDao

    /**
     * Usamos un "Companion Object" para implementar el patrón Singleton.
     * Esto asegura que solo exista UNA instancia de la base de datos
     * en toda la aplicación, evitando problemas de memoria y concurrencia.
     */
    companion object {

        // @Volatile asegura que el valor de INSTANCE sea siempre el más actualizado
        // y visible para todos los hilos de ejecución.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia Singleton de la base de datos.
         * Si no existe, la crea de forma segura (thread-safe).
         */
        fun getDatabase(context: Context): AppDatabase {
            // "synchronized" asegura que solo un hilo pueda ejecutar este bloque
            // a la vez, previniendo que se creen dos instancias de la BD
            // por accidente.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "onefit_database" // Nombre del archivo de la BD en el dispositivo
                )
                    // .fallbackToDestructiveMigration() // Útil en desarrollo si cambias el esquema
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}