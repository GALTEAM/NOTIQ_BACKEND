package com.gal.notiq.domain.evaluation.domain

import com.gal.notiq.domain.evaluation.domain.entity.AnswerEntity
import com.gal.notiq.domain.evaluation.domain.entity.EvaluationEntity
import com.gal.notiq.domain.user.domain.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface EvaluationRepository: JpaRepository<EvaluationEntity, Long> {
    @Query("""
        SELECT a FROM AnswerEntity a
        WHERE (:year IS NULL OR a.year = :year)
          AND (:term IS NULL OR a.term = :term)
          AND (:keyword IS NULL OR a.title LIKE %:keyword%)
    """)
    fun findAllByYearAndTermAndKeyword(
        @Param("year") year: Int?,
        @Param("term") term: Int?,
        @Param("keyword") keyword: String?): List<EvaluationEntity>

    fun findAllByUserEntity(userEntity: UserEntity): List<EvaluationEntity>
    fun existsByTitle(title: String): Boolean
}