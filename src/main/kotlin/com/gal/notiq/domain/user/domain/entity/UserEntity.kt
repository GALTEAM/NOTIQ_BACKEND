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
    val username: String = "", // 유저 아이디

    @Column(nullable = false)
    val name: String = "",

    @Column
    val grade: Int = 0,

    @Column
    val cls: Int = 0,

    @Column
    val num: Int = 0,

    @Column
    var fcmToken: String = "",

    @Column(nullable = false)
    val password: String = "", // Password

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: UserRoles = UserRoles.ROLE_USER

)