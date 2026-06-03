package com.jifelog.security.jwt.api

object JifelogPrincipalUserDataMapper {
    fun toUserData(principal: JifelogPrincipal): JifelogUserData {
        return JifelogUserData(
            userId = principal.userId,
            username = principal.username,
            nickname = principal.nickname,
        )
    }
}
