package com.gal.notiq.domain.evaluation.exception

import com.gal.notiq.global.exception.CustomErrorCode
import org.springframework.http.HttpStatus

enum class EvaluationErrorCode (
    override val status: HttpStatus,
    override val state: String,
    override val message: String,
) : CustomErrorCode {

    EVALUATION_ALREADY_EXIST(HttpStatus.CONFLICT, "CONFLICT", "평가명이 이미 존재합니다"),
    ANSWERSHEET_ALREADY_EXIST(HttpStatus.CONFLICT, "CONFLICT", "가채점표가 이미 저장되었습니다"),
    REGISTER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_SERVER_ERROR","등록 과정에서 에러가 발생했습니다"),
    SHEET_NOT_FOUND(HttpStatus.NOT_FOUND,"NOT_FOUND","양식을 읽을 수 없습니다(null)"),
    EVALUATION_NOT_FOUND(HttpStatus.NOT_FOUND,"NOT_FOUND","평가를 찾을 수 업습니다"),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND,"NOT_FOUND","가채점표를 저장해야 합니다")


}