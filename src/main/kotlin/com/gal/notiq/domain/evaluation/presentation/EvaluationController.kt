package com.gal.notiq.domain.evaluation.presentation

import com.gal.notiq.domain.evaluation.domain.entity.mongo.ContestResultEntity
import com.gal.notiq.domain.evaluation.presentation.dto.request.GetEvaluationsRequest
import com.gal.notiq.domain.evaluation.presentation.dto.request.RegisterEvaluationRequest
import com.gal.notiq.domain.evaluation.presentation.dto.response.GetEvaluationsResponse
import com.gal.notiq.domain.evaluation.presentation.dto.response.GetMyResultResponse
import com.gal.notiq.domain.evaluation.service.EvaluationService
import com.gal.notiq.domain.score.presentation.dto.response.GetMyExamResultResponse
import com.gal.notiq.global.common.BaseResponse
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
    fun getMyEvaluations():BaseResponse<List<GetEvaluationsResponse>> {
        return evaluationService.getMyEvaluations();
    }

    @PostMapping("")
    fun registerEvaluation(@RequestBody request:RegisterEvaluationRequest,
                           @RequestParam file:MultipartFile): BaseResponse<Unit> {
        return evaluationService.register(request,file)
    }

    @GetMapping("")
    fun getEvaluations(@RequestBody request: GetEvaluationsRequest,
                       ): BaseResponse<List<GetEvaluationsResponse>> {
        return evaluationService.getEvaluations(request)
    }

    @GetMapping("/{id}/my-exam")
    fun getMyExamResult(@PathVariable("id") id:Long): BaseResponse<GetMyExamResultResponse>{
        return evaluationService.getMyExamResult(id)
    }

    @GetMapping("/{id}/contest")
    fun getContestResult(@PathVariable("id") id:Long): BaseResponse<List<ContestResultEntity>>{
        return evaluationService.getMyContestResult(id)
    }

    @GetMapping("{id}/my")
    fun getMyResult(@PathVariable("id") id:Long): BaseResponse<GetMyResultResponse>{
        return evaluationService.getMyResult(id)
    }

}