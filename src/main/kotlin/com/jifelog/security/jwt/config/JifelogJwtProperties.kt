package com.jifelog.security.jwt.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jifelog.security.jwt")
data class JifelogJwtProperties(
    val secret: String,
    val issuer: String? = null
)