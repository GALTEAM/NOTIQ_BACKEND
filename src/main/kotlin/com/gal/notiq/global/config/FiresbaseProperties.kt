package com.gal.notiq.global.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "application.firebase")
class FirebaseProperties (
    var id: String? = null,
    var keyPath: String? = null
)