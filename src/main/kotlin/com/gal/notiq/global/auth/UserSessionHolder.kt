package com.gal.notiq.global.auth

import com.gal.notiq.domain.user.domain.model.User

interface UserSessionHolder {
    fun getCurrentUser(): User
}