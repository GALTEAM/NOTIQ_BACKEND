package com.gal.notiq.domain.score.presentation.dto.request

data class RegisterTempScoresRequest(
    val list: List<RegisterTempScoreRequest> = emptyList(),
)