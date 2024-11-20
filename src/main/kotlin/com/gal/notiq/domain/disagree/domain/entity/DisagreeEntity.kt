package com.gal.notiq.domain.disagree.domain.entity

import com.gal.notiq.domain.evaluation.domain.entity.EvaluationEntity
import com.gal.notiq.domain.user.domain.entity.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "tb_disagree")
data class DisagreeEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // ID (PK)

    val comment:String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id")
    val userEntity: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_evaluation_id")
    val evaluationEntity: EvaluationEntity? = null

    )