package com.gal.notiq.domain.evaluation.domain

import com.gal.notiq.domain.evaluation.domain.entity.EvaluationEntity
import com.gal.notiq.domain.user.domain.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface EvaluationRepository: JpaRepository<EvaluationEntity, Long> {
    @Query(value = """
        SELECT * FROM tb_evaluation a 
        WHERE (:year IS NULL OR a.year = :year)
          AND (:term IS NULL OR a.term = :term)
          AND (:keyword IS NULL OR REPLACE(LOWER(a.title),' ','') LIKE LOWER(CONCAT('%', REPLACE(:keyword,' ',''), '%')))
    """, nativeQuery = true)
    fun findAllByYearAndTermAndKeyword(
        @Param("year") year: Int?,
        @Param("term") term: Int?,
        @Param("keyword") keyword: String?): List<EvaluationEntity>

    fun findAllByUserEntity(userEntity: UserEntity): List<EvaluationEntity>
    fun existsByTitle(title: String): Boolean
}