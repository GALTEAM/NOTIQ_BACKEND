package com.gal.notiq.domain.user.presentation.dto.request

data class RegisterUserRequest(
    val username: String = "",
    val name: String = "",
    val password: String = "",
    val grade: Int = 0,
    val cls: Int = 0,
    val num: Int = 0
)
