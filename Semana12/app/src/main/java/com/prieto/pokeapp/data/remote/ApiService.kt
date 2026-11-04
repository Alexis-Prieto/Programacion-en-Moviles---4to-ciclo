package com.prieto.pokeapp.data.remote

import com.prieto.pokeapp.data.remote.dto.ItemDetailDto
import com.prieto.pokeapp.data.remote.dto.PokemonListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("pokemon")
    suspend fun obtenerListaPokemon(
        @Query("limit") limite: Int = 100,
        @Query("offset") offset: Int = 0
    ): PokemonListResponseDto

    @GET("pokemon/{id}")
    suspend fun obtenerDetallePokemon(
        @Path("id") id: Int
    ): ItemDetailDto
}