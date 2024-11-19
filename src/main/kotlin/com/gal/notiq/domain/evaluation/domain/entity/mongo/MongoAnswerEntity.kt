package com.gal.notiq.domain.evaluation.domain.entity.mongo

data class MongoAnswerEntity(
    val id: String? = null,
    val num: Int,
    val correctAnswer: Int,
    val score: Double,
    val isTempScore: Boolean = false
) {
}