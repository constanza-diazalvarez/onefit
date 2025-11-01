package com.example.onefit.model

import com.google.gson.annotations.SerializedName

data class EjercicioDto(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("bodyPart")
    val bodyPart: String?,

    @SerializedName("target")
    val target: String?,

    // Campo adicional para el ID, si la API lo llama diferente
    val exerciseId: String? = null,

    // Campo que añadiste para la URL del GIF
    val imageUrl: String? = null
)
