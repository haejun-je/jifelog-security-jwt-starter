package com.jifelog.security.jwt.support

import com.jifelog.security.jwt.api.JifelogPrincipal
import com.jifelog.security.jwt.api.JifelogPrincipalUserDataMapper
import com.jifelog.security.jwt.api.JifelogUser
import com.jifelog.security.jwt.api.JifelogUserData
import org.springframework.core.MethodParameter
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class JifelogUserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasAnnotation = parameter.hasParameterAnnotation(JifelogUser::class.java)
        val isUserType = JifelogUserData::class.java.isAssignableFrom(parameter.parameterType)
        return hasAnnotation && isUserType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val principal = SecurityContextHolder.getContext().authentication?.principal as? JifelogPrincipal
            ?: throw InsufficientAuthenticationException("Jifelog principal not found in SecurityContext")

        return JifelogPrincipalUserDataMapper.toUserData(principal)
    }
}
