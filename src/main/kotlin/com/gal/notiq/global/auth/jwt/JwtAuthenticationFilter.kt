package com.gal.notiq.global.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.gal.notiq.domain.evaluation.exception.EvaluationErrorCode
import com.gal.notiq.domain.user.exception.UserErrorCode
import com.gal.notiq.global.auth.jwt.exception.JwtErrorCode
import com.gal.notiq.global.auth.jwt.exception.type.JwtErrorType
import com.gal.notiq.global.common.BaseResponse
import com.gal.notiq.global.exception.CustomErrorCode
import com.gal.notiq.global.exception.CustomException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = request.getHeader("Authorization")
//        val path: String = request.servletPath

        if (token.isNullOrEmpty()) {
            filterChain.doFilter(request, response)
            return
        }

        if (!token.startsWith("Bearer ")) {
            setErrorResponse(response, JwtErrorCode.JWT_EMPTY_EXCEPTION)
        } else {
            when (jwtUtils.checkTokenInfo(jwtUtils.getToken(token))) {
                JwtErrorType.OK -> {
                    try{
                        SecurityContextHolder.getContext().authentication = jwtUtils.getAuthentication(token)
                        doFilter(request, response, filterChain)
                    } catch(e: CustomException){
                        setErrorResponse(response, UserErrorCode.USER_NOT_FOUND)
                    }
                }

                JwtErrorType.ExpiredJwtException -> setErrorResponse(response, JwtErrorCode.JWT_TOKEN_EXPIRED)
                JwtErrorType.SignatureException -> setErrorResponse(response, JwtErrorCode.JWT_TOKEN_SIGNATURE_ERROR)
                JwtErrorType.MalformedJwtException -> setErrorResponse(response, JwtErrorCode.JWT_TOKEN_ERROR)
                JwtErrorType.UnsupportedJwtException -> setErrorResponse(
                    response,
                    JwtErrorCode.JWT_TOKEN_UNSUPPORTED_ERROR
                )

                JwtErrorType.IllegalArgumentException -> setErrorResponse(
                    response,
                    JwtErrorCode.JWT_TOKEN_ILL_EXCEPTION
                )

                JwtErrorType.UNKNOWN_EXCEPTION -> setErrorResponse(response, JwtErrorCode.JWT_UNKNOWN_EXCEPTION)
            }
        }
    }

    // error 이거 쓰지 말고 filter로 바꾸던가 JwtErrorCode를 따로 받던가
    private fun setErrorResponse(
        response: HttpServletResponse,
        errorCode: CustomErrorCode
    ) {
        response.status = errorCode.status.value()
        response.contentType = "application/json;charset=UTF-8"

        response.writer.write(
            objectMapper.writeValueAsString(
                BaseResponse<String>(
                    status = errorCode.status.value(),
                    state = errorCode.state,
                    message = errorCode.message
                )
            )
        )
    }
}