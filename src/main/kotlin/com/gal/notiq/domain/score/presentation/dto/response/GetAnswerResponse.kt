package com.gal.notiq.domain.score.presentation.dto.response

import com.gal.notiq.domain.evaluation.domain.entity.mongo.MongoAnswerEntity

data class GetAnswerResponse(
    val num: Int,
    val correctAnswer: Int,
    val score: Double
) {
    companion object {
        fun of(answerEntities: List<MongoAnswerEntity>): List<GetAnswerResponse> {
            return answerEntities.map { entity ->
                GetAnswerResponse(
                    num = entity.num,
                    correctAnswer = entity.correctAnswer,
                    score = entity.score
                )
            }
        }
    }
}