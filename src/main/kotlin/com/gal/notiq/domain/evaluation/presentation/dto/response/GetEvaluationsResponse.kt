package com.gal.notiq.domain.evaluation.presentation.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.gal.notiq.domain.evaluation.domain.entity.EvaluationEntity
import com.gal.notiq.domain.evaluation.domain.enums.EvaluationType
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

data class GetEvaluationsResponse(
    val id: Long?,
    val title: String,
    val year: Int,
    val term: Int,
    val category: EvaluationType,
    val createdDate: String, // LocalDateTime 대신 String
    val name: String?
) {
    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // 원하는 포맷 지정

        fun of(evaluationEntities: List<EvaluationEntity>): List<GetEvaluationsResponse> {
            println("hi")
            return evaluationEntities.map { entity ->
                GetEvaluationsResponse(
                    id = entity.id,
                    title = entity.title,
                    year = entity.year,
                    term = entity.term,
                    category = entity.category,
                    createdDate = entity.createdDate.format(formatter), // LocalDateTime을 String으로 변환
                    name = entity.userEntity?.name
                )
            }
        }
    }
}