package com.gal.notiq.global.config

import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {
    @Bean
    fun httpClient(): HttpClient {
        return HttpClientBuilder.create()
            .setConnectionManager(
                PoolingHttpClientConnectionManagerBuilder.create()
                    .setMaxConnPerRoute(100)
                    .setMaxConnTotal(300)
                    .build()
            )
            .build()
    }

    @Bean
    fun factory(httpClient: HttpClient?): HttpComponentsClientHttpRequestFactory {
        val factory = HttpComponentsClientHttpRequestFactory()
        factory.setConnectTimeout(1500)
        factory.httpClient = httpClient!!
        return factory
    }

    @Bean
    fun restTemplate(factory: HttpComponentsClientHttpRequestFactory?): RestTemplate {
        return RestTemplate(factory!!)
    }
}