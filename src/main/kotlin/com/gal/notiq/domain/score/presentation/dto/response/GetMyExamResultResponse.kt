package com.gal.notiq.domain.score.presentation.dto.response

import com.gal.notiq.domain.evaluation.domain.entity.mongo.MongoAnswerEntity
import com.gal.notiq.domain.score.presentation.dto.request.RegisterTempScoreRequest

data class GetMyExamResultResponse(
    val correctAnswers: List<MongoAnswerEntity>,
    val myAnswers: List<RegisterTempScoreRequest>? = null,
    val score: Double?,
    val tempScore: Double? = null
){

}