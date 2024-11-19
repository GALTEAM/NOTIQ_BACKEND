package com.gal.notiq.domain.evaluation.domain

import com.gal.notiq.domain.evaluation.domain.entity.AnswerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AnswerRepository: JpaRepository<AnswerEntity, Long> {

    @Query("""
        SELECT a FROM AnswerEntity a
        WHERE (:year IS NULL OR a.year = :year)
          AND (:term IS NULL OR a.term = :term)
          AND (:keyword IS NULL OR a.title LIKE %:keyword%)
    """)
    fun findAllByYearAndTermAndKeyword(
        @Param("year") year: Int?,
        @Param("term") term: Int?,
        @Param("keyword") keyword: String?): List<AnswerEntity>

}