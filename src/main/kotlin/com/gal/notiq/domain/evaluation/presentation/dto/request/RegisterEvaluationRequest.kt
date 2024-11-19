package com.gal.notiq.domain.evaluation.presentation.dto.request

import com.gal.notiq.domain.evaluation.domain.enums.EvaluationType

data class RegisterEvaluationRequest(
    val evaluationName:String = "",
    val evaluationType: EvaluationType,
    val year:Int,
    val term:Int,
)