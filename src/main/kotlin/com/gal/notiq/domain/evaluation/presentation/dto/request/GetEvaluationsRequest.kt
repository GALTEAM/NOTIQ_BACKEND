package com.gal.notiq.domain.evaluation.presentation.dto.request

data class GetEvaluationsRequest (
    val year: Int?,
    val term: Int?,
    val keyword: String?
)