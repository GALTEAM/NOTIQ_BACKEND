package com.gal.notiq.domain.evaluation.domain

import com.gal.notiq.domain.evaluation.domain.entity.AnswerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AnswerRepository: JpaRepository<AnswerEntity, Long> {

    @Query(value = """
        SELECT * FROM tb_answer a 
        WHERE (:year IS NULL OR a.year = :year)
          AND (:term IS NULL OR a.term = :term)
          AND (:keyword IS NULL OR REPLACE(LOWER(a.title),' ','') LIKE LOWER(CONCAT('%', REPLACE(:keyword,' ',''), '%')))
    """, nativeQuery = true)
    fun findAllByYearAndTermAndKeyword(
        @Param("year") year: Int?,
        @Param("term") term: Int?,
        @Param("keyword") keyword: String?): List<AnswerEntity>

    fun existsByTitle(title: String): Boolean

}