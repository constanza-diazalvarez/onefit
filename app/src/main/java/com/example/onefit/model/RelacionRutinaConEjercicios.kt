package com.example.onefit.model

import androidx.room.Embedded
import androidx.room.Relation

data class RelacionRutinaConEjercicios(
    @Embedded
    val rutina: Rutina,

    @Relation(
        parentColumn = "id",
        entityColumn = "rutinaId"
    )
    val ejercicios: List<EjercicioRutina>
)
