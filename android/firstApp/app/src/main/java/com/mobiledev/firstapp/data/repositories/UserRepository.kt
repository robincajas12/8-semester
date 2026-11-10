package com.mobiledev.firstapp.data.repositories

import com.mobiledev.firstapp.data.UserDTO

object  UserRepository {

    private val users = mutableListOf(
        UserDTO("Admin", "admin", "https://square.github.io/picasso/static/sample.png"),
        UserDTO("John Doe", "johndoe", "https://i.pravatar.cc/150?img=2"),
        UserDTO("Jane Smith", "janesmith", "https://i.pravatar.cc/150?img=3")
    )

    fun getAllUsers(): List<UserDTO> = users
}
