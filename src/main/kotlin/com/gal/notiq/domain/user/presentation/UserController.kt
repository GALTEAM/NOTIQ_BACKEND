package com.gal.notiq.domain.user.presentation

import com.gal.notiq.domain.user.presentation.dto.request.LoginRequest
import com.gal.notiq.domain.user.presentation.dto.request.RefreshRequest
import com.gal.notiq.domain.user.presentation.dto.request.RegisterUserRequest
import com.gal.notiq.domain.user.presentation.dto.response.GetMyInfoResponse
import com.gal.notiq.global.common.BaseResponse
import com.gal.notiq.domain.user.service.UserService
import com.gal.notiq.global.auth.jwt.JwtInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun registerUser(@RequestBody registerUserRequest: RegisterUserRequest): BaseResponse<Unit> {
        return userService.registerUser(registerUserRequest)
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody loginRequest: LoginRequest): BaseResponse<JwtInfo> {
        return userService.loginUser(loginRequest)
    }

    @PostMapping("/refresh")
    fun refreshUser(@RequestBody refreshRequest: RefreshRequest): BaseResponse<String> {
        return userService.refreshToken(refreshRequest)
    }
    @GetMapping("/my")
    fun getMyInfo(): BaseResponse<GetMyInfoResponse> {
        return userService.getMyInfo();
    }
}