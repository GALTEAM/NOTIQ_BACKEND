package com.gal.notiq.domain.evaluation.domain.entity

import com.gal.notiq.domain.evaluation.domain.enums.EvaluationType
import com.gal.notiq.domain.user.domain.entity.UserEntity
import jakarta.persistence.*
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "tb_evaluation")
@EntityListeners(AuditingEntityListener::class)  // Auditing 이벤트 리스너 활성화
class EvaluationEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // ID (PK)

    @Column(nullable = false,unique = true)
    val title: String,

    @Enumerated(EnumType.STRING)
    val category: EvaluationType,

    val year: Int,

    val term: Int,

    @LastModifiedDate  // 마지막 수정 날짜 자동 관리
    val lastModifiedDate: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id")
    val userEntity: UserEntity

)