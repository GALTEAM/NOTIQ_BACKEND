package com.gal.notiq.domain.score.presentation

import com.gal.notiq.domain.score.presentation.dto.response.GetTempScoresResponse
import com.gal.notiq.domain.score.service.TempScoreService
import com.gal.notiq.domain.score.presentation.dto.request.GetTempScoresRequest
import com.gal.notiq.domain.score.presentation.dto.request.RegisterTempScoreRequest
import com.gal.notiq.domain.score.presentation.dto.response.GetAnswerResponse
import com.gal.notiq.domain.score.presentation.dto.response.RegisterTempScoreResponse
import com.gal.notiq.global.common.BaseResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/temp-score")
class TempScoreController(
    val tempScoreService: TempScoreService
) {

    @GetMapping("") // 가채점표 리스트 반환
    fun getTempScores(@RequestBody request: GetTempScoresRequest):BaseResponse<List<GetTempScoresResponse>>{ // 필터링 한번에 받아서 쿼리로 처리하기
        return tempScoreService.getTempScores(request)
    }

    @GetMapping("/{id}") // 가채점 상세
    fun getTempScore(@PathVariable("id") id:Long):BaseResponse<List<GetAnswerResponse>>{
        return tempScoreService.getTempScore(id)
    }

    @PostMapping("/{id}") // 가채점
    fun registerTempScore(
        @PathVariable("id") id:Long,
        @RequestBody request:List<RegisterTempScoreRequest>):BaseResponse<RegisterTempScoreResponse>{
        return tempScoreService.registerTempScore(id,request);
    }

}