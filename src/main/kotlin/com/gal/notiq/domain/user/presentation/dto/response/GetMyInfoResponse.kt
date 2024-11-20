package com.gal.notiq.domain.user.presentation.dto.response

import com.gal.notiq.domain.user.domain.enums.UserRoles
import com.gal.notiq.domain.user.domain.model.User

data class GetMyInfoResponse(
    val name: String,
    val grade: Int,
    val cls: Int,
    val num: Int,
    val role: UserRoles,
) {
    companion object {
        fun of(user: User): GetMyInfoResponse? {
            return GetMyInfoResponse(
                name = user.name,
                grade = user.grade,
                cls = user.cls,
                num = user.num,
                role = user.role)
        }
    }
}