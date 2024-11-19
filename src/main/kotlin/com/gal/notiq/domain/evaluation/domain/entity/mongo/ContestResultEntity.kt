package com.gal.notiq.domain.evaluation.domain.entity.mongo

data class ContestResultEntity(
    val id: String? = null,
    val result: String,
    val winner:String
) {
}