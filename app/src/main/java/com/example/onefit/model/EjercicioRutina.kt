package com.example.onefit.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ejercicio_rutina",
    foreignKeys = [
        ForeignKey(
            entity = Rutina::class,        // Apunta a la tabla "Padre" (Rutina)
            parentColumns = ["id"],         // La PK de la tabla Rutina
            childColumns = ["rutinaId"],   // La FK en esta tabla "Hija"
            onDelete = ForeignKey.CASCADE   // Si se borra la Rutina "Padre", se borran sus ejercicios "Hijos".
        )
    ],
    indices = [Index(value = ["rutinaId"])]
)
data class EjercicioRutina(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    //FK de Rutina
    val rutinaId: Int,

    @ColumnInfo(name = "nombre_ejercicio")
    val nombreEjercicio: String,

    @ColumnInfo(name = "series")
    val series: Int,

    @ColumnInfo(name = "repeticiones")
    val repeticiones: Int,

    @ColumnInfo(name = "peso")
    val peso: Double? = null
)
