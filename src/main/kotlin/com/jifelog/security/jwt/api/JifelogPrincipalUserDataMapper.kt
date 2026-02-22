package com.jifelog.security.jwt.api

object JifelogPrincipalUserDataMapper {
    fun toUserData(principal: JifelogPrincipal): JifelogUserData {
        return JifelogUserData(
            userId = principal.userId,
            email = principal.email,
            username = principal.username
        )
    }
}
