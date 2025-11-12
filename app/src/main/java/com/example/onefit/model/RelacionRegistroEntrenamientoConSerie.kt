package com.example.onefit.model

import androidx.room.Embedded
import androidx.room.Relation

data class RelacionRegistroEntrenamientoConSerie(
    @Embedded
    val registro: RegistroEntrenamiento,

    @Relation(
        parentColumn = "id",
        entityColumn = "registroSerieEntrenamientoId"
    )
    val series: List<RegistroSerieEntrenamiento>
)
