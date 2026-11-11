package com.example.recyclerview.data.repositories

import com.example.recyclerview.data.dto.UserDto

class UserRepository {

    fun getAllUsers(): List<UserDto> {
        return listOf(
            UserDto(
                name = "John Doe",
                username = "johndoe",
                image = "https://i.pravatar.cc/150?img=1"
            ),
            UserDto(
                name = "Jane Smith",
                username = "janesmith",
                image = "https://i.pravatar.cc/150?img=2"
            ),
            UserDto(
                name = "Michael Brown",
                username = "michaelbrown",
                image = "https://i.pravatar.cc/150?img=3"
            ),
            UserDto(
                name = "Emily Davis",
                username = "emilydavis",
                image = "https://i.pravatar.cc/150?img=4"
            ),
            UserDto(
                name = "David Wilson",
                username = "davidwilson",
                image = "https://i.pravatar.cc/150?img=5"
            )
        )
    }
}
