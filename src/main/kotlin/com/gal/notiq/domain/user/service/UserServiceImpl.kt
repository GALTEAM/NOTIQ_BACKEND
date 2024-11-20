package com.gal.notiq.domain.user.service

import com.gal.notiq.domain.user.domain.UserRepository
import com.gal.notiq.domain.user.domain.entity.UserEntity
import com.gal.notiq.domain.user.domain.mapper.UserMapper
import com.gal.notiq.domain.user.exception.UserErrorCode
import com.gal.notiq.domain.user.presentation.dto.request.LoginRequest
import com.gal.notiq.domain.user.presentation.dto.request.RefreshRequest
import com.gal.notiq.domain.user.presentation.dto.request.RegisterUserRequest
import com.gal.notiq.domain.user.presentation.dto.response.GetMyInfoResponse
import com.gal.notiq.global.auth.UserSessionHolder
import com.gal.notiq.global.auth.jwt.JwtInfo
import com.gal.notiq.global.auth.jwt.JwtUtils
import com.gal.notiq.global.auth.jwt.exception.JwtErrorCode
import com.gal.notiq.global.auth.jwt.exception.type.JwtErrorType
import com.gal.notiq.global.common.BaseResponse
import com.gal.notiq.global.exception.CustomException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userSessionHolder: UserSessionHolder,
    private val userMapper: UserMapper,
    private val bytePasswordEncoder: BCryptPasswordEncoder,
    private val jwtUtils: JwtUtils
) : UserService {

    @Transactional
    override fun registerUser(registerUserRequest: RegisterUserRequest): BaseResponse<Unit> {
        if(userRepository.existsByUsername(registerUserRequest.username) || userRepository.existsByGradeAndClsAndNum(registerUserRequest.grade,registerUserRequest.cls,registerUserRequest.num)) throw CustomException(UserErrorCode.USER_ALREADY_EXIST)

        userRepository.save(
            userMapper.toEntity(
                userMapper.toDomain(registerUserRequest, bytePasswordEncoder.encode(registerUserRequest.password.trim()))
            )
        )

        return BaseResponse(
            message = "회원가입 성공"
        )

    }

    @Transactional(readOnly = true)
    override fun loginUser(loginRequest: LoginRequest): BaseResponse<JwtInfo> {
        val user:UserEntity = userRepository.findByUsername(loginRequest.username)?: throw CustomException(UserErrorCode.USER_NOT_FOUND)
        if (!bytePasswordEncoder.matches(loginRequest.password,user.password)) throw CustomException(UserErrorCode.USER_NOT_MATCH)
        return BaseResponse(
            message = "로그인 성공",
            data = jwtUtils.generate(
                user = userMapper.toDomain(user)
            )
        )
    }

    @Transactional(readOnly = true)
    override fun refreshToken(refreshRequest: RefreshRequest): BaseResponse<String> {
        val token = jwtUtils.getToken(refreshRequest.refreshToken)

        if (jwtUtils.checkTokenInfo(token) == JwtErrorType.ExpiredJwtException) {
            throw CustomException(JwtErrorCode.JWT_TOKEN_EXPIRED)
        }

        // refresh 인지 확인

        val user = userRepository.findByUsername(
            jwtUtils.getUsername(token)
        )

        return BaseResponse (
            message = "리프레시 성공 !",
            data = jwtUtils.refreshToken(
                user = userMapper.toDomain(user!!)
            )
        )
    }

    override fun getMyInfo(): BaseResponse<GetMyInfoResponse> {
        val user = userSessionHolder.getCurrentUser()
        val res = GetMyInfoResponse.of(user)

        return BaseResponse (
            message = "유저 정보 조회",
            data = res
        )
    }
}