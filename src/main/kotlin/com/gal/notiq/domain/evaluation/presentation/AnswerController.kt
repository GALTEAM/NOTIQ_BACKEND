package com.gal.notiq.domain.evaluation.presentation

import com.gal.notiq.domain.evaluation.presentation.dto.request.RegisterEvaluationRequest
import com.gal.notiq.domain.evaluation.service.AnswerService
import com.gal.notiq.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/answer")
class AnswerController(
    private val answerService: AnswerService
) {

    @PostMapping("") // 가채점표 저장
    @Operation(summary = "가채점표 저장", description = "가채점표 저장")
    fun registerAnswerSheet(
        @ModelAttribute request: RegisterEvaluationRequest,
        @RequestParam file:MultipartFile): BaseResponse<Unit> {
        return answerService.registerAnswerSheet(request,file)
    }

}