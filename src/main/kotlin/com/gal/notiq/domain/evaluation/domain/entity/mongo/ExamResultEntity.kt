package com.gal.notiq.domain.evaluation.domain.entity.mongo

data class ExamResultEntity(
    val id: String? = null,
    val grade: Int,
    val cls: Int,
    val num: Int,
    val result: Double
) {
}