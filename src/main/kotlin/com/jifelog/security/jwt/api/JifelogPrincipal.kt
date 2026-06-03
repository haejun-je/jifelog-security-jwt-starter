package com.jifelog.security.jwt.api

import java.security.Principal

data class JifelogPrincipal(
    val userId: String,
    val username: String,
    val nickname: String,
    val roles: Set<String>
) : Principal {
    override fun getName(): String = userId
}
