package com.gal.notiq.domain.score.presentation.dto.response

data class RegisterTempScoreResponse(
    val score: Double
) {
    companion object {
        fun of(compareScore: Double): RegisterTempScoreResponse {
            return RegisterTempScoreResponse(score = compareScore)
        }
    }
}