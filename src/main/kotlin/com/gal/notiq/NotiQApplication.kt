package com.gal.notiq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing  // Auditing 활성화
class NotiQApplication

fun main(args: Array<String>) {
    runApplication<NotiQApplication>(*args)
}
