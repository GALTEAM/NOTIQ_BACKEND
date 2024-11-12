package com.gal.notiq.domain.user.domain.model

import com.gal.notiq.domain.user.domain.enums.UserRoles

data class User(
    val id: Long? = null,
    val username: String = "",
    val name: String = "",
    val password: String = "",
    val grade: Int = 0,
    val cls: Int = 0,
    val num: Int = 0,
    val role: UserRoles = UserRoles.ROLE_USER
)
