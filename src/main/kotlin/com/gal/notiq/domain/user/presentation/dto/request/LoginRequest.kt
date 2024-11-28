package com.gal.notiq.domain.user.presentation.dto.request

data class LoginRequest(
    val username: String = "",
    val password: String = "",
    val fcmToken: String = ""
)
