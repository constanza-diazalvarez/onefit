package com.example.onefit.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registro_entrenamiento")
data class RegistroEntrenamiento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "nombre_rutina")
    val nombreRutina: String,

    @ColumnInfo(name = "fecha")
    val date: Long = System.currentTimeMillis()
)
