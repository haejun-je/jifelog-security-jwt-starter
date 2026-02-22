package com.jifelog.security.jwt.support

import org.springframework.security.core.Authentication

interface JifelogAuthenticationProvider {
    fun parseAuthentication(rawJwt: String): Authentication?
}
