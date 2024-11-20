package com.gal.notiq.domain.evaluation.domain.entity

import com.gal.notiq.domain.user.domain.entity.UserEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.ZoneId

@Entity
@Table(name = "tb_answer")
@EntityListeners(AuditingEntityListener::class)  // Auditing 이벤트 리스너 활성화
class AnswerEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // ID (PK)

    @Column(nullable = false,unique = true)
    val title: String = "",

    val year: Int = 0,

    val term: Int = 0,

    @CreatedDate  // 마지막 수정 날짜 자동 관리
    val createdDate: LocalDateTime? = LocalDateTime.now(ZoneId.of("Asia/Seoul")),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id")
    val userEntity: UserEntity? = null

)