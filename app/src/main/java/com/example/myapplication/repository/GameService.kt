package com.example.myapplication.repository

import com.example.myapplication.repository.model.Game
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GameService {
    @GET("/api/games")
    suspend fun getGameResponse(): Response<List<Game>>
    @GET("/api/game")
    suspend fun getGameDetails(@Query("id") id: String) : Response<Game>
    companion object{
        private const val GAME_URL = "https://www.freetogame.com"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(GAME_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val gameService: GameService by lazy {
            retrofit.create(GameService::class.java)
        }
    }
}