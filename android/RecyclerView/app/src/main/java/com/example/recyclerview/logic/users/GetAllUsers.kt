package com.example.recyclerview.logic.users

import com.example.recyclerview.data.dto.UserDto
import com.example.recyclerview.data.repositories.UserRepository

class GetAllUsers(
    private val userRepository: UserRepository = UserRepository()
) {

    operator fun invoke(): List<UserDto> {
        return userRepository.getAllUsers()
    }
}
