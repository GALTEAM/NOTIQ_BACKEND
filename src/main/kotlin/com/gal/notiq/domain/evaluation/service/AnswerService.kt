package com.gal.notiq.domain.evaluation.service

import com.gal.notiq.domain.evaluation.domain.AnswerRepository
import com.gal.notiq.domain.evaluation.domain.entity.AnswerEntity
import com.gal.notiq.domain.evaluation.domain.entity.mongo.MongoAnswerEntity
import com.gal.notiq.domain.evaluation.exception.EvaluationErrorCode
import com.gal.notiq.domain.evaluation.presentation.dto.request.RegisterEvaluationRequest
import com.gal.notiq.domain.user.domain.entity.UserEntity
import com.gal.notiq.domain.user.domain.mapper.UserMapper
import com.gal.notiq.global.auth.UserSessionHolder
import com.gal.notiq.global.common.BaseResponse
import com.gal.notiq.global.exception.CustomException
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class AnswerService(
    private val answerRepository: AnswerRepository,
    private val userMapper: UserMapper,
    private val userSessionHolder: UserSessionHolder,
    @Qualifier("secondaryMongoTemplate") private val secondaryMongoTemplate: MongoTemplate
) {
    fun registerAnswerSheet(request: RegisterEvaluationRequest,file:MultipartFile): BaseResponse<Unit> {
        val collectionName:String = request.evaluationName
        if(answerRepository.existsByTitle(collectionName)) throw CustomException(EvaluationErrorCode.ANSWERSHEET_ALREADY_EXIST)

        val year:Int = request.year
        val term:Int = request.term

        return registerAnswer(collectionName,year,term,file)
    }

    private fun registerAnswer(collectionName: String, year: Int, term: Int, file: MultipartFile): BaseResponse<Unit> {
        val workbook = XSSFWorkbook(file.inputStream)
        val answerSheet = workbook.getSheet("가채점표")

        if(answerSheet != null) { // 저장 처리
            val user: UserEntity = userMapper.toEntity(userSessionHolder.getCurrentUser());
            for (rowIndex in 1 until answerSheet.physicalNumberOfRows) {
                var num: Int
                var correctAnswer: Int
                var score: Double
                // 추출하기
                try {
                    val row = answerSheet.getRow(rowIndex)

                    num = row.getCell(0).numericCellValue.toInt()
                    correctAnswer = row.getCell(1).numericCellValue.toInt()
                    score = row.getCell(2).numericCellValue

                    val entity = MongoAnswerEntity(num = num, correctAnswer = correctAnswer, score = score)
                    // mongoDB에 저장
                    secondaryMongoTemplate.save(entity, collectionName)
                } catch (e: Throwable) {
                    throw CustomException(EvaluationErrorCode.REGISTER_FAILED)
                }
            }
            answerRepository.save(
                AnswerEntity(
                    title = collectionName,
                    year = year,
                    term = term,
                    userEntity = user
                )
            )

            return BaseResponse(
                message = "가채점표 저장 성공"
            )
        }else {
            throw CustomException(EvaluationErrorCode.SHEET_NOT_FOUND) //"가채점표를 올려야 합니다."
        }
    }


}