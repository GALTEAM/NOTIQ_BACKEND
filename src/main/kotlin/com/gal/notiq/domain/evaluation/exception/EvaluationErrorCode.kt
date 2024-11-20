package com.gal.notiq.domain.evaluation.exception

import com.gal.notiq.global.exception.CustomErrorCode
import org.springframework.http.HttpStatus

enum class EvaluationErrorCode (
    override val status: HttpStatus,
    override val state: String,
    override val message: String,
) : CustomErrorCode {

    EVALUATION_ALREADY_EXIST(HttpStatus.CONFLICT, "CONFLICT", "평가명이 이미 존재합니다"),
    REGISTER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_SERVER_ERROR","등록 과정에서 에러가 발생했습니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND,"NOT_FOUND","지필고사 양식은 가채점표를 포함해야 합니다.")

}