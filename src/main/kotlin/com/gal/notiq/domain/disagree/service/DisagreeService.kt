package com.gal.notiq.domain.disagree.service

import com.gal.notiq.domain.disagree.domain.DisagreeRepository
import com.gal.notiq.domain.disagree.domain.entity.DisagreeEntity
import com.gal.notiq.domain.disagree.presentation.dto.request.RegisterDisagreeRequest
import com.gal.notiq.domain.disagree.presentation.dto.response.GetDisagreesResponse
import com.gal.notiq.domain.evaluation.domain.EvaluationRepository
import com.gal.notiq.domain.user.domain.mapper.UserMapper
import com.gal.notiq.global.auth.UserSessionHolder
import com.gal.notiq.global.common.BaseResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [Exception::class])
class DisagreeService (
    val disagreeRepository: DisagreeRepository,
    val userMapper: UserMapper,
    val userSessionHolder: UserSessionHolder,
    val evaluationRepository: EvaluationRepository
){
    fun registerDisagree(id:Long,request: RegisterDisagreeRequest): BaseResponse<Unit> {
        val userEntity = userMapper.toEntity(userSessionHolder.getCurrentUser())
        val evaluationEntity = evaluationRepository.findById(id).orElse(null)
        disagreeRepository.save(
            DisagreeEntity(
            comment = request.comment,
            userEntity = userEntity,
            evaluationEntity = evaluationEntity
        )
        )

        return BaseResponse(
            message = "이의 저장 성공"
        )
    }

    fun getDisagrees(evaluationId: Long): BaseResponse<List<GetDisagreesResponse>> {
        val res = GetDisagreesResponse.of(disagreeRepository.findAllByEvaluationEntity_Id(evaluationId))

        return BaseResponse(
            message = "이의 저장 성공",
            data = res
        )
    }
}