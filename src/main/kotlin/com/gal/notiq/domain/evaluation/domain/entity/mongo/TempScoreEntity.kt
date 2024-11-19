package com.gal.notiq.domain.evaluation.domain.entity.mongo

import com.gal.notiq.domain.evaluation.domain.entity.AnswerEntity
import com.gal.notiq.domain.score.presentation.dto.request.RegisterTempScoreRequest

data class TempScoreEntity(
    val id: String? = null,
    val username:String,
    val answers: List<RegisterTempScoreRequest>,
    val tempScore: Double,
    val isTempScore: Boolean = true
)