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
    abstract fun rutinaDao(): RutinaDao
    abstract fun registroEntrenamientoDao(): RegistroEntrenamientoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "onefit_database" //nombre del archivo de la BD en el dispositivo
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}