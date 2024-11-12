package com.gal.notiq.domain.user.domain.entity

import com.gal.notiq.domain.user.domain.enums.UserRoles
import jakarta.persistence.*

@Entity
@Table(name = "tb_user")
class UserEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // ID (PK)

    @Column(nullable = false)
    val username: String, // 유저 아이디

    @Column(nullable = false)
    val name: String,

    val grade: Int = 0,

    val cls: Int = 0,

    val num: Int = 0,

    @Column(nullable = false)
    val password: String, // Password

    @Column(nullable = false)
    val role: UserRoles = UserRoles.ROLE_USER

) {
}