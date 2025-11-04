package com.example.onefit.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "registro_serie_entrenamiento",
    foreignKeys = [
        ForeignKey(
            entity = RegistroEntrenamiento::class,
            parentColumns = ["id"],
            childColumns = ["registroSerieEntrenamientoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["registroSerieEntrenamientoId"])]
)
data class RegistroSerieEntrenamiento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val registroSerieEntrenamientoId: Int,

    @ColumnInfo(name = "nombre_ejercicio")
    val nombreEjercicio: String,

    @ColumnInfo(name = "numero_serie")
    val numeroSerie: Int,

    @ColumnInfo(name = "repeticiones")
    val repeticiones: Int,

    @ColumnInfo(name = "peso")
    val peso: Double?
)
