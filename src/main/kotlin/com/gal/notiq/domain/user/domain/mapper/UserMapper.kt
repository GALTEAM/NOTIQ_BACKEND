package com.gal.notiq.domain.user.domain.mapper

import com.gal.notiq.domain.user.domain.entity.UserEntity
import com.gal.notiq.domain.user.domain.enums.UserRoles
import com.gal.notiq.domain.user.domain.model.User
import com.gal.notiq.global.common.Mapper
import com.gal.notiq.domain.user.presentation.dto.request.RegisterUserRequest
import org.springframework.stereotype.Component

@Component
class UserMapper(
): Mapper<User, UserEntity> {
    override fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            username = entity.username,
            name = entity.name,
            grade = entity.grade,
            cls = entity.cls,
            num = entity.num,
            password = entity.password,
            role = entity.role
        )
    }

    override fun toEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            username = domain.username,
            name = domain.name,
            grade = domain.grade,
            cls = domain.cls,
            num = domain.num,
            password = domain.password,
            role = domain.role
        )
    }

    fun toDomain(
        registerUserRequest: RegisterUserRequest,
        password: String
    ): User {
        return User(
            username = registerUserRequest.username,
            name = registerUserRequest.name,
            password = password,
            grade = registerUserRequest.grade,
            cls =  registerUserRequest.cls,
            num = registerUserRequest.num
        )
    }
}