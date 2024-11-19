package com.gal.notiq.domain.disagree.presentation.dto.response

import com.gal.notiq.domain.disagree.domain.entity.DisagreeEntity

data class GetDisagreesResponse(
    val id: Long?,
    val comment: String,
    val name: String
) {
    companion object {
        fun of(disagreeEntities: List<DisagreeEntity>): List<GetDisagreesResponse> {
            return disagreeEntities.map { entity ->
                GetDisagreesResponse(
                    id = entity.id,
                    comment = entity.comment,
                    name = entity.userEntity.name
                )
            }
        }
    }
}