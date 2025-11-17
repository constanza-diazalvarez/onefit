package com.example.onefit.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

import com.example.onefit.model.AppDatabase
import com.example.onefit.model.EjercicioRutina
import com.example.onefit.model.RegistroEntrenamientoDao
import com.example.onefit.model.Rutina
import com.example.onefit.model.RutinaDao
import com.example.onefit.model.RutinasRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
        callback: RoomDatabase.Callback
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "onefit_database"
        )
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideRutinaDao(database: AppDatabase): RutinaDao = database.rutinaDao()

    @Provides
    fun provideRegistroEntrenamientoDao(database: AppDatabase): RegistroEntrenamientoDao =
        database.registroEntrenamientoDao()

    @Provides
    @Singleton
    fun provideRutinasRepository(
        rutinaDao: RutinaDao,
        registroDao: RegistroEntrenamientoDao
    ): RutinasRepository {
        return RutinasRepository(rutinaDao, registroDao)
    }

    @Provides
    @Singleton
    fun provideDatabaseCallback(
        daoProvider: Provider<RutinaDao>
    ) = object : RoomDatabase.Callback() {
        private val applicationScope = CoroutineScope(Dispatchers.IO)

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            applicationScope.launch {
                val rutinaDao = daoProvider.get()

                val idPierna = rutinaDao.insertRutina(Rutina(nombre = "Pierna", descripcion = "Cuádriceps-Femorales-Gluteos"))
                rutinaDao.insertEjercicioRutina(EjercicioRutina(rutinaId = idPierna.toInt(), nombreEjercicio = "Sentadilla", series = 4, repeticiones = 10))
                rutinaDao.insertEjercicioRutina(EjercicioRutina(rutinaId = idPierna.toInt(), nombreEjercicio = "Peso Muerto", series = 4, repeticiones = 10))
                rutinaDao.insertEjercicioRutina(EjercicioRutina(rutinaId = idPierna.toInt(), nombreEjercicio = "Búlgara", series = 4, repeticiones = 10))

                val idPecho = rutinaDao.insertRutina(Rutina(nombre = "Push", descripcion = "Pecho-Hombro-Triceps"))
                rutinaDao.insertEjercicioRutina(EjercicioRutina(rutinaId = idPecho.toInt(), nombreEjercicio = "Press Banca", series = 4, repeticiones = 10))
                rutinaDao.insertEjercicioRutina(EjercicioRutina(rutinaId = idPecho.toInt(), nombreEjercicio = "Press Banca Inclinado", series = 4, repeticiones = 10))
                rutinaDao.insertEjercicioRutina(EjercicioRutina(rutinaId = idPecho.toInt(), nombreEjercicio = "Press Militar", series = 3, repeticiones = 10))

                val idEspalda = rutinaDao.insertRutina(Rutina(nombre = "Pull", descripcion = "Espalda-Biceps"))
                rutinaDao.insertEjercicioRutina(EjercicioRutina(rutinaId = idEspalda.toInt(), nombreEjercicio = "Remo con Barra", series = 4, repeticiones = 10))
                rutinaDao.insertEjercicioRutina(EjercicioRutina(rutinaId = idEspalda.toInt(), nombreEjercicio = "Jalón al Pecho", series = 4, repeticiones = 10))
                rutinaDao.insertEjercicioRutina(EjercicioRutina(rutinaId = idEspalda.toInt(), nombreEjercicio = "Curl de Biceps", series = 4, repeticiones = 10))
            }
        }
    }
}
