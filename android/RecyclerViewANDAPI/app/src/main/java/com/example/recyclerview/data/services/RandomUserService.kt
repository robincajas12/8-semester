package com.example.recyclerview.data.services

import com.example.recyclerview.data.dto.RandomUserDTO
import retrofit2.http.GET

interface RandomUserService {
    @GET("api/?results=5")
    suspend fun listRepos(): RandomUserDTO
}
