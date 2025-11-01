package com.example.onefit.api
import retrofit2.http.GET
import com.example.onefit.model.EjercicioDto
import retrofit2.http.Query
import com.example.onefit.model.ImagenEjercicio

interface EjercicioApiService {
    @GET("exercises")
    suspend fun obtenerEjercicios(): List<EjercicioDto>

    @GET("exercises/image")
    suspend fun obtenerImagenEjercicio(
        @Query("exerciseId") exerciseId: String,
        @Query("resolution") resolution: String = "180"
    ): ImagenEjercicio
}