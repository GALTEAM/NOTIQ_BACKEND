package com.gal.notiq.domain.fcm.dto


data class Message (
    val notification: Notification,
    val token: String?,
)