package com.example.onefit.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.onefit.model.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
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

    /*
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
        // 1. YA NO PEDIMOS EL CALLBACK AQUÍ
    ): AppDatabase {

        // 2. DEFINIMOS EL CALLBACK AQUÍ ADENTRO
        val databaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {


                    db.execSQL("INSERT INTO rutina (nombre_rutina, descripcion) VALUES('Pierna', 'Cuádriceps-Femorales-Gluteos')")
                    db.execSQL("INSERT INTO rutina (nombre_rutina, descripcion) VALUES('Push', 'Pecho-Hombro-Triceps')")
                    db.execSQL("INSERT INTO rutina (nombre_rutina, descripcion) VALUES('Pull', 'Espalda-Biceps')")


                    // Ejercicios para Pierna (ID=1)
                    db.execSQL("INSERT INTO ejercicio_rutina (rutinaId, nombreEjercicio, series, repeticiones) VALUES(1, 'Sentadilla', 4, 10)")
                    db.execSQL("INSERT INTO ejercicio_rutina (rutinaId, nombreEjercicio, series, repeticiones) VALUES(1, 'Peso Muerto', 4, 10)")
                    db.execSQL("INSERT INTO ejercicio_rutina (rutinaId, nombreEjercicio, series, repeticiones) VALUES(1, 'Búlgara', 4, 10)")

                    // Ejercicios para Push (ID=2)
                    db.execSQL("INSERT INTO ejercicio_rutina (rutinaId, nombreEjercicio, series, repeticiones) VALUES(2, 'Press Banca', 4, 10)")
                    db.execSQL("INSERT INTO ejercicio_rutina (rutinaId, nombreEjercicio, series, repeticiones) VALUES(2, 'Press Banca Inclinado', 4, 10)")
                    db.execSQL("INSERT INTO ejercicio_rutina (rutinaId, nombreEjercicio, series, repeticiones) VALUES(2, 'Press Militar', 3, 10)")

                    // Ejercicios para Pull (ID=3)
                    db.execSQL("INSERT INTO ejercicio_rutina (rutinaId, nombreEjercicio, series, repeticiones) VALUES(3, 'Remo con Barra', 4, 10)")
                    db.execSQL("INSERT INTO ejercicio_rutina (rutinaId, nombreEjercicio, series, repeticiones) VALUES(3, 'Jalón al Pecho', 4, 10)")
                    db.execSQL("INSERT INTO ejercicio_rutina (rutinaId, nombreEjercicio, series, repeticiones) VALUES(3, 'Curl de Biceps', 4, 10)")
                }
            }
        }

        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "onefit_database"
        )
            .addCallback(databaseCallback)
            .build()
    }*/

    @Provides
    fun provideRutinaDao(database: AppDatabase): RutinaDao = database.rutinaDao()

    @Provides
    fun provideRegistroEntrenamientoDao(database: AppDatabase): RegistroEntrenamientoDao =
        database.registroEntrenamientoDao()

    /*
     Hilt usará el @Inject constructor de la clase RutinasRepository.

    @Provides
    @Singleton
    fun provideRutinasRepository(
        rutinaDao: RutinaDao,
        registroDao: RegistroEntrenamientoDao
    ): RutinasRepository {
        return RutinasRepository(rutinaDao, registroDao)
    }*/

    @Provides
    @Singleton
    fun provideDatabaseCallback(
        daoProvider: Provider<RutinaDao>,
        registroDaoProvider: Provider<RegistroEntrenamientoDao>
    ) = object : RoomDatabase.Callback() {
        private val applicationScope = CoroutineScope(Dispatchers.IO)

        private fun getMilisegundos(dia: Int, mes: Int, anio: Int): Long {
            return LocalDate.of(anio, mes, dia)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            applicationScope.launch {
                val rutinaDao = daoProvider.get()
                val registroDao = registroDaoProvider.get()

                val idPierna = rutinaDao.insertRutina(
                    Rutina(
                        nombre = "Pierna",
                        descripcion = "Cuádriceps-Femorales-Gluteos"
                    )
                )
                rutinaDao.insertEjercicioRutina(
                    EjercicioRutina(
                        rutinaId = idPierna.toInt(),
                        nombreEjercicio = "Sentadilla",
                        series = 4,
                        repeticiones = 10,
                        peso = null
                    )
                )
                rutinaDao.insertEjercicioRutina(
                    EjercicioRutina(
                        rutinaId = idPierna.toInt(),
                        nombreEjercicio = "Peso Muerto",
                        series = 4,
                        repeticiones = 10,
                        peso = null
                    )
                )
                rutinaDao.insertEjercicioRutina(
                    EjercicioRutina(
                        rutinaId = idPierna.toInt(),
                        nombreEjercicio = "Búlgara",
                        series = 3,
                        repeticiones = 10,
                        peso = null
                    )
                )

                val idPecho = rutinaDao.insertRutina(
                    Rutina(
                        nombre = "Push",
                        descripcion = "Pecho-Hombro-Triceps"
                    )
                )
                rutinaDao.insertEjercicioRutina(
                    EjercicioRutina(
                        rutinaId = idPecho.toInt(),
                        nombreEjercicio = "Press Banca",
                        series = 4,
                        repeticiones = 10,
                        peso = null
                    )
                )
                rutinaDao.insertEjercicioRutina(
                    EjercicioRutina(
                        rutinaId = idPecho.toInt(),
                        nombreEjercicio = "Press Banca Inclinado",
                        series = 4,
                        repeticiones = 10,
                        peso = null
                    )
                )
                rutinaDao.insertEjercicioRutina(
                    EjercicioRutina(
                        rutinaId = idPecho.toInt(),
                        nombreEjercicio = "Press Militar",
                        series = 3,
                        repeticiones = 10,
                        peso = null
                    )
                )

                val idEspalda =
                    rutinaDao.insertRutina(Rutina(nombre = "Pull", descripcion = "Espalda-Biceps"))
                rutinaDao.insertEjercicioRutina(
                    EjercicioRutina(
                        rutinaId = idEspalda.toInt(),
                        nombreEjercicio = "Remo con Barra",
                        series = 4,
                        repeticiones = 10,
                        peso = null
                    )
                )
                rutinaDao.insertEjercicioRutina(
                    EjercicioRutina(
                        rutinaId = idEspalda.toInt(),
                        nombreEjercicio = "Jalón al Pecho",
                        series = 4,
                        repeticiones = 10,
                        peso = null
                    )
                )
                rutinaDao.insertEjercicioRutina(
                    EjercicioRutina(
                        rutinaId = idEspalda.toInt(),
                        nombreEjercicio = "Curl de Biceps",
                        series = 4,
                        repeticiones = 10,
                        peso = null
                    )
                )

                val idLogPush = registroDao.insertRegistroEntrenamiento(
                    RegistroEntrenamiento(
                        nombreRutina = "Push",
                        date = getMilisegundos(10, 11, 2025) // 10 Nov 2025
                    )
                )
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Banca", numeroSerie = 1, repeticiones = 10, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Banca", numeroSerie = 2, repeticiones = 8, peso = 40.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Banca", numeroSerie = 3, repeticiones = 6, peso = 45.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Banca", numeroSerie = 4, repeticiones = 4, peso = 50.0))

                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Banca Inclinado", numeroSerie = 1, repeticiones = 10, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Banca Inclinado", numeroSerie = 2, repeticiones = 8, peso = 30.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Banca Inclinado", numeroSerie = 3, repeticiones = 6, peso = 35.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Banca Inclinado", numeroSerie = 4, repeticiones = 4, peso = 35.0))

                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Militar", numeroSerie = 1, repeticiones = 8, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Militar", numeroSerie = 2, repeticiones = 8, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Militar", numeroSerie = 3, repeticiones = 8, peso = 20.0))

                val idLogPull = registroDao.insertRegistroEntrenamiento(
                    RegistroEntrenamiento(
                        nombreRutina = "Pull",
                        date = getMilisegundos(12, 11, 2025) // 12 Nov 2025
                    )
                )
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPull.toInt(), nombreEjercicio = "Remo con Barra", numeroSerie = 1, repeticiones = 10, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPull.toInt(), nombreEjercicio = "Remo con Barra", numeroSerie = 2, repeticiones = 6, peso = 50.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPull.toInt(), nombreEjercicio = "Remo con Barra", numeroSerie = 3, repeticiones = 6, peso = 55.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPull.toInt(), nombreEjercicio = "Remo con Barra", numeroSerie = 4, repeticiones = 4, peso = 60.0))

                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPull.toInt(), nombreEjercicio = "Jalón al Pecho", numeroSerie = 1, repeticiones = 6, peso = 30.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPull.toInt(), nombreEjercicio = "Jalón al Pecho", numeroSerie = 2, repeticiones = 6, peso = 35.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPull.toInt(), nombreEjercicio = "Jalón al Pecho", numeroSerie = 3, repeticiones = 4, peso = 40.0))

                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPull.toInt(), nombreEjercicio = "Curl de Biceps", numeroSerie = 1, repeticiones = 6, peso = 10.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPull.toInt(), nombreEjercicio = "Curl de Biceps", numeroSerie = 2, repeticiones = 6, peso = 10.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPull.toInt(), nombreEjercicio = "Curl de Biceps", numeroSerie = 3, repeticiones = 6, peso = 10.0))

                val idLogPierna = registroDao.insertRegistroEntrenamiento(
                    RegistroEntrenamiento(
                        nombreRutina = "Pierna",
                        date = getMilisegundos(14, 11, 2025) // 14 Nov 2025
                    )
                )
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Sentadilla", numeroSerie = 1, repeticiones = 10, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Sentadilla", numeroSerie = 2, repeticiones = 6, peso = 40.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Sentadilla", numeroSerie = 3, repeticiones = 6, peso = 45.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Sentadilla", numeroSerie = 4, repeticiones = 6, peso = 50.0))

                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Peso Muerto", numeroSerie = 1, repeticiones = 10, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Peso Muerto", numeroSerie = 2, repeticiones = 6, peso = 50.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Peso Muerto", numeroSerie = 3, repeticiones = 6, peso = 55.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Peso Muerto", numeroSerie = 4, repeticiones = 3, peso = 60.0))

                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Búlgara", numeroSerie = 1, repeticiones = 6, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Búlgara", numeroSerie = 2, repeticiones = 6, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPierna.toInt(), nombreEjercicio = "Búlgara", numeroSerie = 3, repeticiones = 6, peso = 20.0))

                val idLogPush2 = registroDao.insertRegistroEntrenamiento(
                    RegistroEntrenamiento(
                        nombreRutina = "Push",
                        date = getMilisegundos(17, 11, 2025) // 10 Nov 2025
                    )
                )
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush2.toInt(), nombreEjercicio = "Press Banca", numeroSerie = 1, repeticiones = 10, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush2.toInt(), nombreEjercicio = "Press Banca", numeroSerie = 2, repeticiones = 8, peso = 40.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush2.toInt(), nombreEjercicio = "Press Banca", numeroSerie = 3, repeticiones = 6, peso = 45.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush.toInt(), nombreEjercicio = "Press Banca", numeroSerie = 4, repeticiones = 4, peso = 50.0))

                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush2.toInt(), nombreEjercicio = "Press Banca Inclinado", numeroSerie = 1, repeticiones = 10, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush2.toInt(), nombreEjercicio = "Press Banca Inclinado", numeroSerie = 2, repeticiones = 8, peso = 30.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush2.toInt(), nombreEjercicio = "Press Banca Inclinado", numeroSerie = 3, repeticiones = 6, peso = 35.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush2.toInt(), nombreEjercicio = "Press Banca Inclinado", numeroSerie = 4, repeticiones = 4, peso = 35.0))

                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush2.toInt(), nombreEjercicio = "Press Militar", numeroSerie = 1, repeticiones = 8, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush2.toInt(), nombreEjercicio = "Press Militar", numeroSerie = 2, repeticiones = 8, peso = 20.0))
                registroDao.insertSerie(RegistroSerieEntrenamiento(registroSerieEntrenamientoId = idLogPush2.toInt(), nombreEjercicio = "Press Militar", numeroSerie = 3, repeticiones = 8, peso = 20.0))
            }
        }
    }
}
