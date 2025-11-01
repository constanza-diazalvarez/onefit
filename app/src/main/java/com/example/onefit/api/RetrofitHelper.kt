package com.example.onefit.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.onefit.BuildConfig
import okhttp3.OkHttpClient

object RetrofitHelper {
    private const val BASE_URL = "https://exercisedb.p.rapidapi.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-RapidAPI-Key", BuildConfig.RAPID_API_KEY)
                .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
                .build()
            chain.proceed(request)
        }
        .build()

    val api: EjercicioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EjercicioApiService::class.java)
    }
}

