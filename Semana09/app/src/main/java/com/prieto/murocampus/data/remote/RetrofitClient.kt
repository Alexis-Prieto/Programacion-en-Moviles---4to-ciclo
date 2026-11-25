package com.prieto.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {

    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val cliente = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val peticionOriginal = chain.request()
            val peticionJson = peticionOriginal.newBuilder()
                .header("Accept", "application/json")
                .build()
            chain.proceed(peticionJson)
        }
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    val api: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(cliente)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(PostApi::class.java)
    }
}