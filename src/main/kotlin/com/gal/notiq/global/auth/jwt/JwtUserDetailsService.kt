package com.gal.notiq.global.auth.jwt

import com.gal.notiq.domain.user.domain.UserRepository
import com.gal.notiq.domain.user.domain.mapper.UserMapper
import com.gal.notiq.domain.evaluation.exception.EvaluationErrorCode
import com.gal.notiq.domain.user.exception.UserErrorCode
import com.gal.notiq.global.exception.CustomException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JwtUserDetailsService (
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(email: String): UserDetails {
        return JwtUserDetails (
            user = userMapper.toDomain(
               entity = userRepository.findByUsername(email)?: throw CustomException(UserErrorCode.USER_NOT_FOUND)
            )
        )
    }

}