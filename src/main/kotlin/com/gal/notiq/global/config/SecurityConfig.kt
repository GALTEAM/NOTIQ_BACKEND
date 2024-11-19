package com.gal.notiq.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.gal.notiq.global.auth.jwt.JwtUtils
import com.gal.notiq.global.auth.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val jwtUtils: JwtUtils,
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors {
                corsConfigurationSource()
            }

            .csrf {
                it.disable()
            }

            .formLogin {
                it.disable()
            }

            .sessionManagement { session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }

            .authorizeHttpRequests {
                it
                    .requestMatchers("/user/**").permitAll()
                    .requestMatchers("/user/my").authenticated()
                    .requestMatchers("/evaluation/my").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.POST,"/evaluation").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.GET,"/evaluation").authenticated()
                    .requestMatchers("/evaluation/{id}/**").hasAuthority("ROLE_USER")
                    .requestMatchers(HttpMethod.POST,"/disagree/").hasAuthority("ROLE_USER")
                    .requestMatchers(HttpMethod.GET,"/disagree/{evaluationId}").hasAuthority("ROLE_ADMIN")
                    .requestMatchers(HttpMethod.GET,"/temp-score").authenticated()
                    .requestMatchers("/temp-score/{id}").hasAuthority("ROLE_USER")
                    .requestMatchers("/swagger-ui/**", "/v3/**").permitAll()
                    .anyRequest().permitAll()
            }

            .addFilterBefore(JwtAuthenticationFilter(jwtUtils, objectMapper), UsernamePasswordAuthenticationFilter::class.java)

            .exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.NOT_FOUND))
            }

            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedOriginPattern("*")
        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.addAllowedMethod("*")
        corsConfiguration.allowCredentials = true

        val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)

        return urlBasedCorsConfigurationSource
    }

}