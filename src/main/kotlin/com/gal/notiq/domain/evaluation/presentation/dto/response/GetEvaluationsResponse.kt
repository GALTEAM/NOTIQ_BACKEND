package com.gal.notiq.domain.evaluation.presentation.dto.response

import com.gal.notiq.domain.evaluation.domain.entity.EvaluationEntity
import com.gal.notiq.domain.evaluation.domain.enums.EvaluationType
import java.time.LocalDateTime

data class GetEvaluationsResponse(
    val id: Long?,
    val title: String,
    val year:Int,
    val term:Int,
    val category: EvaluationType,
    val lastModifiedDate: LocalDateTime?,
    val name: String?
) {
    companion object {
        fun of(evaluationEntities: List<EvaluationEntity>): List<GetEvaluationsResponse> {
            return evaluationEntities.map { entity ->
                GetEvaluationsResponse(
                    id = entity.id,
                    title = entity.title,
                    year = entity.year,
                    term = entity.term,
                    category = entity.category,
                    lastModifiedDate = entity.createdDate,
                    name = entity.userEntity?.name
                )
            }
        }
    }
}