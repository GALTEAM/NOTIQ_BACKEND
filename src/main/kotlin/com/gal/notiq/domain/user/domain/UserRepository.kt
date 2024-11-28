package com.gal.notiq.domain.user.domain

import com.gal.notiq.domain.user.domain.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
    fun existsByUsername(username: String): Boolean
    fun existsByGradeAndClsAndNum(grade: Int, cls: Int, num: Int): Boolean

    @Query("select u from UserEntity u where u.role = 'ROLE_USER' and u.fcmToken != ''")
    fun findAllByFcmToken(): List<UserEntity>
}