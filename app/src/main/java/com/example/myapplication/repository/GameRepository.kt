package com.example.myapplication.repository

import com.example.myapplication.repository.model.Game
import retrofit2.Response

class GameRepository {
    suspend fun getGameResponse(): Response<List<Game>> =
            GameService.gameService.getGameResponse()

    suspend fun getGameDetails(id: String): Response<Game> = GameService.gameService.getGameDetails(id = id)
}