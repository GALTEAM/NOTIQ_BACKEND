package com.gal.notiq.domain.evaluation.service

import com.gal.notiq.domain.evaluation.domain.*
import com.gal.notiq.domain.evaluation.domain.entity.AnswerEntity
import com.gal.notiq.domain.evaluation.domain.entity.EvaluationEntity
import com.gal.notiq.domain.evaluation.domain.entity.mongo.*
import com.gal.notiq.domain.evaluation.domain.enums.EvaluationType
import com.gal.notiq.domain.evaluation.exception.EvaluationErrorCode
import com.gal.notiq.domain.evaluation.presentation.dto.request.GetEvaluationsRequest
import com.gal.notiq.domain.evaluation.presentation.dto.request.RegisterEvaluationRequest
import com.gal.notiq.domain.evaluation.presentation.dto.response.GetEvaluationsResponse
import com.gal.notiq.domain.evaluation.presentation.dto.response.GetMyResultResponse
import com.gal.notiq.domain.score.presentation.dto.response.GetMyExamResultResponse
import com.gal.notiq.domain.score.service.TempScoreService
import com.gal.notiq.domain.user.domain.entity.UserEntity
import com.gal.notiq.domain.user.domain.mapper.UserMapper
import com.gal.notiq.domain.user.exception.UserErrorCode
import com.gal.notiq.global.auth.UserSessionHolder
import com.gal.notiq.global.common.BaseResponse
import com.gal.notiq.global.exception.CustomException
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*


@Service
@Transactional(rollbackFor = [Exception::class])
class EvaluationService(
    val evaluationRepository: EvaluationRepository,
    val answerRepository: AnswerRepository,
    val userMapper: UserMapper,
    val userSessionHolder: UserSessionHolder,
    val mongoTemplate: MongoTemplate,
    @Qualifier("secondaryMongoTemplate") private val secondaryMongoTemplate: MongoTemplate,
    private val tempScoreService: TempScoreService,
) {
    fun register(request:RegisterEvaluationRequest, file:MultipartFile): BaseResponse<Unit> {
        // 1. 이미 있는 평가인지 확인
        val collectionName:String = request.evaluationName;
        if(evaluationRepository.existsByTitle(collectionName)) throw CustomException(EvaluationErrorCode.EVALUATION_ALREADY_EXIST)

        val year:Int = request.year
        val term:Int = request.term

        // 1. 일단 if 처리
        when(request.evaluationType){
            EvaluationType.EXAM -> registerExam(collectionName,year,term,file);
            EvaluationType.CONTEST -> registerContest(collectionName,year,term,file);
            EvaluationType.ETC -> registerEtc(collectionName,year,term,file);
        }

        return BaseResponse(
            message = "평가 저장 성공"
        )
    }

    private fun registerContest(collectionName: String, year: Int, term: Int, file: MultipartFile) {
        val workbook = XSSFWorkbook(file.inputStream)
        val sheet = workbook.getSheet("대회결과")
        val user:UserEntity = userMapper.toEntity(userSessionHolder.getCurrentUser())

        var result: String
        var winner: String
        for (rowIndex in 1 until sheet.physicalNumberOfRows) {
            try {
                val row = sheet.getRow(rowIndex)

                result = row.getCell(0).stringCellValue
                winner = row.getCell(1).stringCellValue

                val entity = ContestResultEntity(
                    result = result,
                    winner = winner)
                mongoTemplate.save(entity,collectionName)
            } catch (e: Throwable) {
                throw CustomException(EvaluationErrorCode.REGISTER_FAILED)
            }
        }
        evaluationRepository.save(
            EvaluationEntity(
                title = collectionName,
                year = year,
                category = EvaluationType.CONTEST,
                term = term,
                userEntity = user)
        )
    }

    private fun registerEtc(collectionName: String, year: Int, term: Int, file: MultipartFile) {
        val workbook = XSSFWorkbook(file.inputStream)
        val sheet = workbook.getSheet("기타결과")

        if(sheet != null){
            val user:UserEntity = userMapper.toEntity(userSessionHolder.getCurrentUser())
            var grade: String
            var cls: String
            var num: String
            val rowCnt:Int = sheet.getRow(1).getCell(0).numericCellValue.toInt()
            println(rowCnt)

            for (i in 0..sheet.lastRowNum) { // i <= sheet.getLastRowNum()와 동일
                val row = sheet.getRow(i)
                if (row != null) {
                    if(i == 0){
                        grade = "학년"
                        cls = "반"
                        num = "번호"
                    } else {
                        grade = row.getCell(1).numericCellValue.toInt().toString() // 첫 번째 셀 (학년)
                        cls = row.getCell(2).numericCellValue.toInt().toString()   // 두 번째 셀 (반)
                        num = row.getCell(3).numericCellValue.toInt().toString()   // 세 번째 셀 (번호)
                    }

                    // List<String> 형태로 나머지 정보를 저장
                    val additionalData = mutableListOf<String>()
                    for (j in 4 until rowCnt) { // rowCnt + 1 (X)
                        if(i==0){
                            additionalData.add(row.getCell(j).stringCellValue)
                        }else {
                            additionalData.add(row.getCell(j).rawValue.toString())
                        }
                    }

                    // StudentScore 객체 생성
                    val entity = EtcResultEntity(
                        grade = grade,
                        cls = cls,
                        num = num,
                        value = additionalData)

                    mongoTemplate.save(entity, collectionName)
                }
            }
            evaluationRepository.save(EvaluationEntity(
                title = collectionName,
                year = year,
                category = EvaluationType.ETC,
                term = term,
                userEntity = user
            ))
        }
    }

    private fun registerExam(collectionName:String,year:Int,term:Int,file:MultipartFile) {
        val workbook = XSSFWorkbook(file.inputStream)
        val answerSheet = workbook.getSheet("가채점표");
        val scoreSheet = workbook.getSheet("성적표");

        // 시트 이름으로 구분
        // 1. 가채점 시트
        if(answerSheet != null){ // 저장 처리
            val user:UserEntity = userMapper.toEntity(userSessionHolder.getCurrentUser());
            if(scoreSheet != null) { // 2. 성적표 // 저장 처리
                var grade: Int
                var cls: Int
                var num: Int
                var result: Double
                for (rowIndex in 1 until scoreSheet.physicalNumberOfRows) {
                    // 추출하기
//                    try {
                        val row = scoreSheet.getRow(rowIndex)

                        if (rowIndex == 1) {
                            grade = row.getCell(0).numericCellValue.toInt()
                            cls = row.getCell(1).numericCellValue.toInt()
                            num = row.getCell(2).numericCellValue.toInt()
                            result = row.getCell(3).numericCellValue

                            val entity = ExamResultEntity(
                                grade = grade,
                                cls = cls,
                                num = num,
                                result = result
                            )

                            println("여기 존재")
                            // mongoDB에 저장
                            mongoTemplate.save(entity, collectionName)
                            println("통과")
                        }
//                    } catch (e: Throwable) {
//                        throw CustomException(EvaluationErrorCode.REGISTER_FAILED)
//                    }
                }
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
            }
            evaluationRepository.save(
                EvaluationEntity(
                    title = collectionName,
                    year = year,
                    category = EvaluationType.EXAM,
                    term = term,
                    userEntity = user
                )
            )
            answerRepository.save(
                AnswerEntity(
                    title = collectionName,
                    year = year,
                    term = term,
                    userEntity = user
                )
            )
        }else {
            throw CustomException(EvaluationErrorCode.ANSWER_NOT_FOUND) //"가채점표를 올려야 합니다."
        }

    }

    fun getEvaluations(request:GetEvaluationsRequest): BaseResponse<List<GetEvaluationsResponse>> { // 평가 리스트 받기
        val entity:List<EvaluationEntity> = evaluationRepository.findAllByYearAndTermAndKeyword(request.year, request.term,request.keyword)
        println(entity)
        val res = GetEvaluationsResponse.of(entity)

        return BaseResponse(
            message = "평가 리스트 조회 성공",
            data = res
        )
    }

    fun getMyExamResult(id:Long): BaseResponse<GetMyExamResultResponse> {
        val collectionName = evaluationRepository.findById(id)
        if(collectionName.isPresent){
            val collectionName = collectionName.get().title
            val correctAnswers = tempScoreService.findAnswerByCollectionName(collectionName)
            val user = userSessionHolder.getCurrentUser()
            val myTempScore = tempScoreService.findAnswerByUsername(collectionName,user.username)
            val myScore = findMyScore(user.grade,user.cls,user.num,collectionName);

            val res = GetMyExamResultResponse(
                correctAnswers = correctAnswers,
                myAnswers = myTempScore?.answers,
                score = myScore?.result,
                tempScore = myTempScore?.tempScore
            )

            return BaseResponse(
                message = "지필평가 결과 조회 성공",
                data = res
            )
        }
        throw CustomException(EvaluationErrorCode.EVALUATION_NOT_FOUND)
    }

    fun findMyScore(grade:Int,cls:Int,num:Int,collectionName:String): ExamResultEntity? {
        val query = Query()
        // 조건 추가
        query.addCriteria(Criteria.where("grade").`is`(grade))
        query.addCriteria(Criteria.where("cls").`is`(cls))
        query.addCriteria(Criteria.where("num").`is`(num))

        return mongoTemplate.findOne(query, ExamResultEntity::class.java,collectionName)
    }

    fun getMyContestResult(id:Long): BaseResponse<List<ContestResultEntity>> {
        val evaluation = evaluationRepository.findById(id)
        if(evaluation.isPresent){
            val collectionName = evaluation.get().title
            val res = findContestResult(collectionName)

            return BaseResponse(
                message = "대회 결과 조회 성공",
                data = res
            )
        }
        throw CustomException(EvaluationErrorCode.EVALUATION_NOT_FOUND)
    }

    private fun findContestResult(collectionName: String): List<ContestResultEntity>? {
        val query = Query()
        return mongoTemplate.find(query, ContestResultEntity::class.java,collectionName)
    }

    fun getMyResult(id: Long): BaseResponse<GetMyResultResponse> {
        val evaluation = evaluationRepository.findById(id)
        if(evaluation.isPresent){
            val collectionName = evaluation.get().title

            val user = userSessionHolder.getCurrentUser()

            val menu = findMenu(collectionName)
            val myResult = findMyResult(user.grade,user.cls,user.num,collectionName)

            val res = GetMyResultResponse(
                menu = menu?.value,
                myResult = myResult?.value
            )

            return BaseResponse(
                message = "기타 결과 조회 성공",
                data = res
            )
        }
        throw CustomException(EvaluationErrorCode.EVALUATION_NOT_FOUND)
    }

    private fun findMyResult(grade:Int,cls:Int,num:Int,collectionName: String): EtcResultEntity? {
        val query = Query()

        query.addCriteria(Criteria.where("grade").`is`(grade.toString()))
        query.addCriteria(Criteria.where("cls").`is`(cls.toString()))
        query.addCriteria(Criteria.where("num").`is`(num.toString()))

        return mongoTemplate.findOne(query, EtcResultEntity::class.java,collectionName)
    }

    private fun findMenu(collectionName: String): EtcResultEntity? {
        val query = Query()

        query.addCriteria(Criteria.where("grade").`is`("학년"))
        query.addCriteria(Criteria.where("cls").`is`("반"))
        query.addCriteria(Criteria.where("num").`is`("번호"))
        return mongoTemplate.findOne(query, EtcResultEntity::class.java,collectionName)
    }

    fun getMyEvaluations(): BaseResponse<List<GetEvaluationsResponse>> {
        val userEntity = userMapper.toEntity(userSessionHolder.getCurrentUser())
        val res = GetEvaluationsResponse.of(evaluationRepository.findAllByUserEntity(userEntity))

        return BaseResponse(
            message = "내 평가 리스트 조회",
            data = res
        )
    }


}