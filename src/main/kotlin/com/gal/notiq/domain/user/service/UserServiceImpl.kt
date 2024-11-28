package com.gal.notiq.domain.user.service

import com.gal.notiq.domain.fcm.dto.FCMRequest
import com.gal.notiq.domain.fcm.dto.Message
import com.gal.notiq.domain.fcm.dto.Notification
import com.gal.notiq.domain.user.domain.UserRepository
import com.gal.notiq.domain.user.domain.entity.UserEntity
import com.gal.notiq.domain.user.domain.enums.UserRoles
import com.gal.notiq.domain.user.domain.mapper.UserMapper
import com.gal.notiq.domain.user.exception.UserErrorCode
import com.gal.notiq.domain.user.presentation.dto.request.LoginRequest
import com.gal.notiq.domain.user.presentation.dto.request.RefreshRequest
import com.gal.notiq.domain.user.presentation.dto.request.RegisterUserRequest
import com.gal.notiq.domain.user.presentation.dto.response.GetMyInfoResponse
import com.gal.notiq.global.auth.UserSessionHolder
import org.springframework.web.reactive.function.client.WebClient
import com.gal.notiq.global.auth.jwt.JwtInfo
import com.gal.notiq.global.auth.jwt.JwtUtils
import com.gal.notiq.global.auth.jwt.exception.JwtErrorCode
import com.gal.notiq.global.auth.jwt.exception.type.JwtErrorType
import com.gal.notiq.global.common.BaseResponse
import com.gal.notiq.global.config.FirebaseProperties
import com.gal.notiq.global.exception.CustomException
import com.google.auth.oauth2.GoogleCredentials
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.io.IOException
import java.io.InputStream

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userSessionHolder: UserSessionHolder,
    private val userMapper: UserMapper,
    private val bytePasswordEncoder: BCryptPasswordEncoder,
    private val jwtUtils: JwtUtils,
    private val firebaseProperties: FirebaseProperties,
) : UserService {

    @Transactional
    override fun registerUser(registerUserRequest: RegisterUserRequest): BaseResponse<Unit> {
        if(userRepository.existsByUsername(registerUserRequest.username) || userRepository.existsByGradeAndClsAndNum(registerUserRequest.grade,registerUserRequest.cls,registerUserRequest.num)) throw CustomException(
            UserErrorCode.USER_ALREADY_EXIST)

        userRepository.save(
            userMapper.toEntity(
                userMapper.toDomain(registerUserRequest, bytePasswordEncoder.encode(registerUserRequest.password.trim()))
            )
        )

        return BaseResponse(
            message = "회원가입 성공"
        )

    }

    @Transactional
    override fun loginUser(loginRequest: LoginRequest): BaseResponse<JwtInfo> {
        val user:UserEntity = userRepository.findByUsername(loginRequest.username)?: throw CustomException(
            UserErrorCode.USER_NOT_FOUND)
        if (!bytePasswordEncoder.matches(loginRequest.password,user.password)) throw CustomException(UserErrorCode.USER_NOT_MATCH)
        if(loginRequest.fcmToken != "") {
            user.fcmToken = loginRequest.fcmToken
            userRepository.save(user)
        }

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

    override fun alarm(evaluationName: String) {
        try {
            val scopes: MutableList<String> = ArrayList()
            scopes.add("https://www.googleapis.com/auth/firebase.messaging")
            scopes.add("https://www.googleapis.com/auth/cloud-platform")

            val inputStream: InputStream = getResourceFileAsInputStream(firebaseProperties.keyPath)
            val gc: GoogleCredentials = GoogleCredentials.fromStream(inputStream).createScoped(scopes)
            val token: String = gc.refreshAccessToken().tokenValue

            println(firebaseProperties.keyPath)
            println(firebaseProperties.id)

            val url = String.format("https://fcm.googleapis.com/v1/projects/%s/messages:send",firebaseProperties.id)

            val n: Notification = Notification("새로운 가채점 업로드!", String.format("%s 의 점수를 확인해보세요", evaluationName))

            // Emulator
//            Message m = new Message(n, "cBsZOVlYVUPcjbChvQ-X6s:APA91bGROh6mOE4T9rYoqb1ewI8KA0lStLH5pSRD7PGVbOBdY0_Y2S04uo1AngPPD1vbniCv4HTXlWXvqwy-ISxlREjkWEzF0uOT_Cc1Jo2VJhaQK6vrCPek4EYAmRPp6bZBBuVhLn_b");
            val users = userRepository.findAllByFcmToken()
            // Physical Device
            for(user in users){
                val m: Message = Message(
                    n,
                    user.fcmToken
                )
                // Every Device
                //Message m = new Message(n, "News");
                val r: FCMRequest = FCMRequest(m)

                // POST 요청
                val webClient: WebClient = WebClient.builder().build()
                val response: String? = webClient.post()
                    .uri(url) // url 정의
                    .header("Authorization", "Bearer $token")
                    .bodyValue(r) // requestBody 정의
                    .retrieve() // 응답 정의 시작
                    .bodyToMono(String::class.java) // 응답 데이터 정의
                    .block() // 동기식 처리

                println(response)
            }

            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getResourceFileAsInputStream(fileName: String?): InputStream {
        val classLoader = UserService::class.java.classLoader
        return classLoader.getResourceAsStream(fileName)
    }
}