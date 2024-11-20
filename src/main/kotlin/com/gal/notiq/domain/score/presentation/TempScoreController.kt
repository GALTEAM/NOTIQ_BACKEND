package com.gal.notiq.domain.score.presentation

import com.gal.notiq.domain.score.presentation.dto.response.GetTempScoresResponse
import com.gal.notiq.domain.score.service.TempScoreService
import com.gal.notiq.domain.score.presentation.dto.request.GetTempScoresRequest
import com.gal.notiq.domain.score.presentation.dto.request.RegisterTempScoreRequest
import com.gal.notiq.domain.score.presentation.dto.request.RegisterTempScoresRequest
import com.gal.notiq.domain.score.presentation.dto.response.GetAnswerResponse
import com.gal.notiq.domain.score.presentation.dto.response.RegisterTempScoreResponse
import com.gal.notiq.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/temp-score")
class TempScoreController(
    val tempScoreService: TempScoreService
) {

    @GetMapping("") // 가채점표 리스트 반환
    @Operation(summary = "가채점 가능한 평가 리스트 반환", description = "가채점 가능한 평가 리스트 반환(authenticated)")
    fun getTempScores(@RequestBody request: GetTempScoresRequest):BaseResponse<List<GetTempScoresResponse>>{ // 필터링 한번에 받아서 쿼리로 처리하기
        return tempScoreService.getTempScores(request)
    }

    @GetMapping("/{id}") // 가채점 상세
    @Operation(summary = "가채점 상세", description = "정답버호, 점수 리스트 (user)")
    fun getTempScore(@PathVariable("id") id:Long):BaseResponse<List<GetAnswerResponse>>{
        return tempScoreService.getTempScore(id)
    }

    @PostMapping("/{id}") // 가채점
    @Operation(summary = "가채점", description = "가채점한 점수를 저장한 후 반환")
    fun registerTempScore(
        @PathVariable("id") id:Long,
        @RequestBody request: RegisterTempScoresRequest):BaseResponse<RegisterTempScoreResponse>{
        println("entered")
        return tempScoreService.registerTempScore(id,request);
    }

}