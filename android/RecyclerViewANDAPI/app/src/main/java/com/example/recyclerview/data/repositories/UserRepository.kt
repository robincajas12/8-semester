package com.example.recyclerview.data.repositories

import com.example.recyclerview.data.dto.RandomUserDTO
import com.example.recyclerview.data.services.RandomUserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {

    private val service: RandomUserService by lazy {
        Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RandomUserService::class.java)
    }

    suspend fun getAllUsers(): RandomUserDTO = service.listRepos()
}
