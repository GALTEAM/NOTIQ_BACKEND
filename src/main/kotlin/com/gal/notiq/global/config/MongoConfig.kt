package com.gal.notiq.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory


@Configuration
class MongoConfig(
    @Value("\${data.mongodb.evaluation}") private val evaluationDB: String,
    @Value("\${data.mongodb.temp-score}") private val tempScoreDB: String
){

    @Primary // 기본 템플릿으로 지정
    @Bean
    fun primaryMongoTemplate(): MongoTemplate {
        val factory: MongoDatabaseFactory =
            SimpleMongoClientDatabaseFactory(evaluationDB)
        return MongoTemplate(factory)
    }

    @Bean // 쓰고 싶을때 @Qualifer 사용
    // @Qualifier("secondaryMongoTemplate") private val secondaryMongoTemplate: MongoTemplate
    fun secondaryMongoTemplate(): MongoTemplate {
        val factory: MongoDatabaseFactory =
            SimpleMongoClientDatabaseFactory(tempScoreDB)
        return MongoTemplate(factory)
    }
}