package com.gal.notiq.domain.evaluation.domain.entity.mongo

data class EtcResultEntity(
    val id: String? = null,
    val grade: String,
    val cls:String,
    val num:String,
    val value:List<String>
) {
}