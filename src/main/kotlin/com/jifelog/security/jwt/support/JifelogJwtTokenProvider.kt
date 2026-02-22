package com.jifelog.security.jwt.support

import com.jifelog.security.jwt.api.JifelogPrincipal
import com.jifelog.security.jwt.config.JifelogJwtProperties
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.text.ParseException
import java.time.Instant

class JifelogJwtTokenProvider(
    private val jwtProperties: JifelogJwtProperties
) : JifelogAuthenticationProvider {
    override fun parseAuthentication(rawJwt: String): Authentication? {
        val jwt = try {
            SignedJWT.parse(rawJwt)
        } catch (_: ParseException) {
            return null
        }

        if (jwt.header.algorithm != JWSAlgorithm.HS256) {
            return null
        }

        val verifier = try {
            MACVerifier(jwtProperties.secret.toByteArray())
        } catch (_: JOSEException) {
            return null
        }

        val isVerified = try {
            jwt.verify(verifier)
        } catch (_: JOSEException) {
            false
        }

        if (!isVerified) {
            return null
        }

        val claims = jwt.jwtClaimsSet
        val expiresAt = claims.expirationTime?.toInstant()

        if (expiresAt == null || Instant.now().isAfter(expiresAt)) {
            return null
        }

        val issuer = jwtProperties.issuer
        if (issuer != null && issuer != claims.issuer) {
            return null
        }

        val userId = claims.getStringClaim("id")
        val email = claims.getStringClaim("em")
        val username = claims.getStringClaim("un")
        val roles = extractRoles(claims.getClaim("roles"))

        val principal = JifelogPrincipal(
            userId = userId,
            email = email,
            username = username,
            roles = roles
        )

        val authorities = roles.map(::SimpleGrantedAuthority)

        return UsernamePasswordAuthenticationToken(principal, null, authorities)
    }

    private fun extractRoles(rawRoles: Any?): Set<String> {
        if (rawRoles !is Collection<*>) {
            return emptySet()
        }

        return rawRoles
            .asSequence()
            .filterIsInstance<String>()
            .map(String::trim)
            .filter(String::isNotEmpty)
            .toSet()
    }
}
