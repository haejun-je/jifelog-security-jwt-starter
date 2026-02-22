package com.jifelog.security.jwt.filter

import com.jifelog.security.jwt.support.JifelogAuthenticationProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JifelogJwtAuthFilter(
    private val authenticationProvider: JifelogAuthenticationProvider
) : OncePerRequestFilter() {
    companion object {
        const val JWT_HEADER = "X-Jifelog-JWT"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val context = SecurityContextHolder.getContext()
        val rawJwt = request.getHeader(JWT_HEADER)?.trim()

        if (!rawJwt.isNullOrEmpty() && context.authentication == null) {
            val authentication = authenticationProvider.parseAuthentication(rawJwt)
            if (authentication != null) {
                context.authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}