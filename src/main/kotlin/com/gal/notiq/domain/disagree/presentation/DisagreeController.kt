package com.gal.notiq.domain.disagree.presentation

import com.gal.notiq.domain.disagree.presentation.dto.request.RegisterDisagreeRequest
import com.gal.notiq.domain.disagree.presentation.dto.response.GetDisagreesResponse
import com.gal.notiq.domain.disagree.service.DisagreeService
import com.gal.notiq.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/disagree")
class DisagreeController(
    private val disagreeService: DisagreeService
) {

    @PostMapping("/{evaluationId}")
    @Operation(summary = "이의 등록", description = "이의 등록(user)")
    fun registerDisagree(@PathVariable("evaluationId") id:Long,@RequestBody request:RegisterDisagreeRequest):BaseResponse<Unit>{
        return disagreeService.registerDisagree(id,request)
    }

    @GetMapping("/{evaluationId}")
    @Operation(summary = "이의 제기 조회", description = "평가ID로 등록된 이의제기 리스트 조회 (admin)")
    fun getDisagrees(@PathVariable evaluationId:Long):BaseResponse<List<GetDisagreesResponse>> {
        return disagreeService.getDisagrees(evaluationId)
    }

}