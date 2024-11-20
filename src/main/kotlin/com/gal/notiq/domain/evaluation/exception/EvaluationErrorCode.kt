package com.gal.notiq.domain.evaluation.exception

import com.gal.notiq.global.exception.CustomErrorCode
import org.springframework.http.HttpStatus

enum class EvaluationErrorCode (
    override val status: HttpStatus,
    override val state: String,
    override val message: String,
) : CustomErrorCode {

    EVALUATION_ALREADY_EXIST(HttpStatus.CONFLICT, "CONFLICT", "평가명이 이미 존재합니다")
}