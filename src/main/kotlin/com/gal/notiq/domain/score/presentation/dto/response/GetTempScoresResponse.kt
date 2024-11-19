package com.gal.notiq.domain.score.presentation.dto.response

import com.gal.notiq.domain.evaluation.domain.entity.AnswerEntity
import java.time.LocalDateTime

data class GetTempScoresResponse(
    val id: Long?,
    val title: String,
    val year:Int,
    val term:Int,
    val lastModifiedDate: LocalDateTime?,
    val name: String
) {
    companion object {
        fun of(answerEntities: List<AnswerEntity>): List<GetTempScoresResponse> {
            return answerEntities.map { entity ->
                GetTempScoresResponse(
                    id = entity.id,
                    title = entity.title,
                    year = entity.year,
                    term = entity.term,
                    lastModifiedDate = entity.lastModifiedDate,
                    name = entity.userEntity.name
                )
            }
        }
    }
}