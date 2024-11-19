package com.gal.notiq.domain.disagree.presentation

import com.gal.notiq.domain.disagree.presentation.dto.request.RegisterDisagreeRequest
import com.gal.notiq.domain.disagree.presentation.dto.response.GetDisagreesResponse
import com.gal.notiq.domain.disagree.service.DisagreeService
import com.gal.notiq.global.common.BaseResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/disagree")
class DisagreeController(
    private val disagreeService: DisagreeService
) {

    @PostMapping("/{evaluationId}")
    fun registerDisagree(@PathVariable("evaluationId") id:Long,@RequestBody request:RegisterDisagreeRequest):BaseResponse<Unit>{
        return disagreeService.registerDisagree(id,request)
    }

    @GetMapping("/{evaluationId}")
    fun getDisagrees(@PathVariable evaluationId:Long):BaseResponse<List<GetDisagreesResponse>> {
        return disagreeService.getDisagrees(evaluationId)
    }

}