package com.gal.notiq.domain.score.presentation.dto.request

data class GetTempScoresRequest(
    val year: Int?,
    val term: Int?,
    val keyword: String?
)