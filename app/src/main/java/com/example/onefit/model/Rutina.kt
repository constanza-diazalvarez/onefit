package com.example.onefit.model

data class Rutina(
    val id: Int,
    val nombreEjercicio: String,
    val series: Int,
    val repeticiones: Int,
    val peso: Double?
)