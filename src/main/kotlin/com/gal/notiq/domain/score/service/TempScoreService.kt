package com.gal.notiq.domain.score.service

import com.gal.notiq.domain.evaluation.domain.AnswerRepository
import com.gal.notiq.domain.evaluation.domain.entity.mongo.MongoAnswerEntity
import com.gal.notiq.domain.evaluation.domain.entity.mongo.TempScoreEntity
import com.gal.notiq.domain.score.presentation.dto.response.GetTempScoresResponse
import com.gal.notiq.domain.score.presentation.dto.request.GetTempScoresRequest
import com.gal.notiq.domain.score.presentation.dto.request.RegisterTempScoreRequest
import com.gal.notiq.domain.score.presentation.dto.request.RegisterTempScoresRequest
import com.gal.notiq.domain.score.presentation.dto.response.GetAnswerResponse
import com.gal.notiq.domain.score.presentation.dto.response.RegisterTempScoreResponse
import com.gal.notiq.global.auth.UserSessionHolder
import com.gal.notiq.global.common.BaseResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class TempScoreService(
    val answerRepository: AnswerRepository,
    val userSessionHolder: UserSessionHolder,
    @Qualifier("secondaryMongoTemplate") private val secondaryMongoTemplate: MongoTemplate,
) {
    fun getTempScores(request: GetTempScoresRequest): BaseResponse<List<GetTempScoresResponse>> {
        val res:List<GetTempScoresResponse> = GetTempScoresResponse.of(answerRepository.findAllByYearAndTermAndKeyword(request.year, request.term,request.keyword))

        return BaseResponse(
            message = "가채점 리스트 조회 성공",
            data = res
        )
    }

    fun getTempScore(id: Long): BaseResponse<List<GetAnswerResponse>> {
        val collectionName = answerRepository.findById(id).orElse(null).title
        val res = GetAnswerResponse.of(findAnswerByCollectionName(collectionName))
        return BaseResponse(
            message = "가채점 상세 조회 성공",
            data = res
        )
    }

    fun findAnswerByCollectionName(collectionName:String): List<MongoAnswerEntity> {
        val query = Query()

        // 조건 추가
        query.addCriteria(Criteria.where("isTempScore").`is`(false))
//        term?.let {
//            query.addCriteria(Criteria.where("term").`is`(it))
//        }
//        keyword?.let {
//            query.addCriteria(Criteria.where("keyword").regex(".*$it.*", "i")) // 부분 일치, 대소문자 무시
//        }

        // 쿼리 실행
        return secondaryMongoTemplate.find(query, MongoAnswerEntity::class.java,collectionName)
    }

    fun registerTempScore(id: Long, requests: RegisterTempScoresRequest): BaseResponse<RegisterTempScoreResponse> {
        val collectionName = answerRepository.findById(id).orElse(null).title
        val correctAnswers = findAnswerByCollectionName(collectionName)
        val tempScore = compareScore(correctAnswers,requests.list)
        val username = userSessionHolder.getCurrentUser().username

        secondaryMongoTemplate.save(TempScoreEntity(username = username,answers = requests.list, tempScore = tempScore),collectionName);

        return BaseResponse(
            message = "가채점 성공",
            data = RegisterTempScoreResponse.of(tempScore)
        )
    }

    private fun compareScore(correctAnswers: List<MongoAnswerEntity>, requests: List<RegisterTempScoreRequest>): Double {
        var score = 0.0
        for(i in correctAnswers.indices) {
            if(correctAnswers[i].correctAnswer == requests[i].answer){
                score += correctAnswers[i].score
            }
        }
        return score
    }

    fun findAnswerByUsername(collectionName:String,username:String): TempScoreEntity? {
        val query = Query()

        // 조건 추가
        query.addCriteria(Criteria.where("username").`is`(username))

        // 쿼리 실행
        return secondaryMongoTemplate.findOne(query, TempScoreEntity::class.java,collectionName)
    }

}