package com.gal.notiq.domain.disagree.domain

import com.gal.notiq.domain.disagree.domain.entity.DisagreeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DisagreeRepository: JpaRepository<DisagreeEntity, Long> {
    fun findAllByEvaluationEntity_Id(evaluationId: Long): List<DisagreeEntity>
}