package com.mobiledev.firstapp.logic.userUseCase

import com.mobiledev.firstapp.data.UserDTO
import com.mobiledev.firstapp.data.repositories.UserRepository

class GetAllUsersUserCase {
    operator fun invoke(): List<UserDTO> = UserRepository.getAllUsers()
}
