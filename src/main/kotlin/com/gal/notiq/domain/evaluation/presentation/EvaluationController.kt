package com.gal.notiq.domain.evaluation.presentation

import com.gal.notiq.domain.evaluation.domain.entity.mongo.ContestResultEntity
import com.gal.notiq.domain.evaluation.presentation.dto.request.GetEvaluationsRequest
import com.gal.notiq.domain.evaluation.presentation.dto.request.RegisterEvaluationRequest
import com.gal.notiq.domain.evaluation.presentation.dto.response.GetEvaluationsResponse
import com.gal.notiq.domain.evaluation.presentation.dto.response.GetMyResultResponse
import com.gal.notiq.domain.evaluation.service.EvaluationService
import com.gal.notiq.domain.score.presentation.dto.response.GetMyExamResultResponse
import com.gal.notiq.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/evaluation")
class EvaluationController(
    private val evaluationService:EvaluationService
) {

    @GetMapping("/my")
    @Operation(summary = "내 평가 리스트 조회", description = "내가 올린 평가 리스트 조회 (admin)")
    fun getMyEvaluations():BaseResponse<List<GetEvaluationsResponse>> {
        return evaluationService.getMyEvaluations();
    }

    @PostMapping("")
    @Operation(summary = "평가 등록", description = "평가 등록 (admin)")
    fun registerEvaluation(@RequestBody request:RegisterEvaluationRequest,
                           @RequestParam file:MultipartFile): BaseResponse<Unit> {
        return evaluationService.register(request,file)
    }

    @GetMapping("")
    @Operation(summary = "평가 리스트 조회", description = "평가 리스트 조회 , 기본조회는 다 null로(admin)")
    fun getEvaluations(@RequestBody request: GetEvaluationsRequest,
                       ): BaseResponse<List<GetEvaluationsResponse>> {
        return evaluationService.getEvaluations(request)
    }

    @GetMapping("/{id}/my-exam")
    @Operation(summary = "지필고사 결과 조회", description = "지필고사 결과를 조회 (user)")
    fun getMyExamResult(@PathVariable("id") id:Long): BaseResponse<GetMyExamResultResponse>{
        return evaluationService.getMyExamResult(id)
    }

    @GetMapping("/{id}/contest")
    @Operation(summary = "대회 결과 조회", description = "대회 결과 조회 (user)")
    fun getContestResult(@PathVariable("id") id:Long): BaseResponse<List<ContestResultEntity>>{
        return evaluationService.getMyContestResult(id)
    }

    @GetMapping("{id}/my")
    @Operation(summary = "기타 결과 조회", description = "기타 결과 조회 (user)")
    fun getMyResult(@PathVariable("id") id:Long): BaseResponse<GetMyResultResponse>{
        return evaluationService.getMyResult(id)
    }

}