package com.gal.notiq.domain.user.service

import com.gal.notiq.domain.user.presentation.dto.request.LoginRequest
import com.gal.notiq.domain.user.presentation.dto.request.RefreshRequest
import com.gal.notiq.domain.user.presentation.dto.request.RegisterUserRequest
import com.gal.notiq.domain.user.presentation.dto.response.GetMyInfoResponse
import com.gal.notiq.global.auth.jwt.JwtInfo
import com.gal.notiq.global.common.BaseResponse

interface UserService {
    fun registerUser(registerUserRequest: RegisterUserRequest): BaseResponse<Unit>
    fun loginUser(loginRequest: LoginRequest): BaseResponse<JwtInfo>
    fun refreshToken(refreshRequest: RefreshRequest): BaseResponse<String>
    fun getMyInfo(): BaseResponse<GetMyInfoResponse>
    fun alarm(evaluationName:String)
}