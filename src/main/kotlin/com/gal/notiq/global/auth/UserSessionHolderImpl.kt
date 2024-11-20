package com.gal.notiq.global.auth

import com.gal.notiq.global.auth.jwt.JwtUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import com.gal.notiq.domain.user.domain.model.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class UserSessionHolderImpl : UserSessionHolder{
    override fun getCurrentUser(): User {
        // 현재 인증 정보 가져오기
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalStateException("No authentication found in SecurityContext")

        // authentication이 null인지 체크

        // principal을 JwtUserDetails로 안전하게 캐스팅
        val principal = authentication.principal

        // principal이 JwtUserDetails인지 확인
        if (principal is JwtUserDetails) {
            return principal.user // User 정보를 반환
        } else {
            throw IllegalStateException("Authentication principal is not of type JwtUserDetails")
        }
    }
}