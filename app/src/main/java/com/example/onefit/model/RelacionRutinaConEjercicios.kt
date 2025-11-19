package com.example.onefit.model

import androidx.room.Embedded
import androidx.room.Relation

data class RelacionRutinaConEjercicios(
    @Embedded
    val rutina: Rutina,

    @Relation(
        parentColumn = "id", //la columna del padre rutina
        entityColumn = "rutinaId" //la columna del hijo ejercicioRutina
    )
    val ejercicios: List<EjercicioRutina>
)
