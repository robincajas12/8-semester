package com.example.recyclerview.logic.users

import com.example.recyclerview.data.dto.RandomUserDTO
import com.example.recyclerview.data.repositories.UserRepository

class GetAllUsers(
    private val userRepository: UserRepository = UserRepository()
) {
    suspend operator fun invoke(): RandomUserDTO = userRepository.getAllUsers()
}
