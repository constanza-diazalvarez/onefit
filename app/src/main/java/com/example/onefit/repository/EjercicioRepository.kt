package com.example.onefit.repository

import com.example.onefit.api.RetrofitHelper

suspend fun probarLlamada() {
    try {
        val ejercicios = RetrofitHelper.api.obtenerEjercicios()
        println("Recibí ${ejercicios.size} ejercicios")
        ejercicios.forEach {
            println("Ejercicio: ${it.name} — ID: ${it.exerciseId}")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

