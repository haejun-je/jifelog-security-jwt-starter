package com.jifelog.security.jwt.config

import com.jifelog.security.jwt.filter.JifelogJwtAuthFilter
import com.jifelog.security.jwt.support.JifelogAuthenticationProvider
import com.jifelog.security.jwt.support.JifelogJwtTokenProvider
import com.jifelog.security.jwt.support.JifelogUserArgumentResolver
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.method.support.HandlerMethodArgumentResolver

@AutoConfiguration
@ConditionalOnClass(OncePerRequestFilter::class)
@EnableConfigurationProperties(JifelogJwtProperties::class)
class JwtSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun jifelogJwtAuthFilter(
        authenticationProvider: JifelogAuthenticationProvider
    ): JifelogJwtAuthFilter {
        return JifelogJwtAuthFilter(authenticationProvider)
    }

    @Bean
    @ConditionalOnMissingBean
    fun jifelogJwtTokenProvider(
        jwtProperties: JifelogJwtProperties
    ): JifelogAuthenticationProvider {
        return JifelogJwtTokenProvider(jwtProperties)
    }

    @Bean
    @ConditionalOnMissingBean
    fun jifelogUserArgumentResolver(): HandlerMethodArgumentResolver {
        return JifelogUserArgumentResolver()
    }
}