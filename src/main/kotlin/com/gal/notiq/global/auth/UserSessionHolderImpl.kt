package com.gal.notiq.global.auth

import com.gal.notiq.global.auth.jwt.JwtUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import com.gal.notiq.domain.user.domain.model.User
import org.springframework.stereotype.Service

@Service
class UserSessionHolderImpl : UserSessionHolder{
    override fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication

        val principal:JwtUserDetails = authentication.details as JwtUserDetails
        return principal.user
    }
}