package com.gal.notiq.domain.evaluation.domain.entity

import com.gal.notiq.domain.evaluation.domain.enums.EvaluationType
import com.gal.notiq.domain.user.domain.entity.UserEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.ZoneId

@Entity
@Table(name = "tb_evaluation")
@EntityListeners(AuditingEntityListener::class)  // Auditing 이벤트 리스너 활성화
class EvaluationEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // ID (PK)

    @Column(nullable = false,unique = true)
    val title: String = "",

    @Enumerated(EnumType.STRING)
    val category: EvaluationType = EvaluationType.ETC,

    val year: Int = 0,

    val term: Int = 0,

    @CreatedDate  // 생성일
    val createdDate: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id")
    val userEntity: UserEntity? = null

)