/**
 * Copyright 2020 Leito. All Rights Reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import modelos.Rol
import org.slf4j.LoggerFactory
import seguridad.Lector
import utils.Env
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class Angel {
    /**
     * Funciones estáticas de seguridad
     */
    companion object {
        private val logger = LoggerFactory.getLogger(Angel::class.java)
        private var publicKey: RSAPublicKey? = null
        private var privateKey: RSAPrivateKey? = null
        private var leidas = false
    }


    /**
     * validaciones a las consultas realizadas a los endpoinds del Ingeniero Guillermo
     *
     * @param req puntero http de intercambio de la peticion actual
     * @return verdadero en caso de que sea válido, falso en caso contrario
     */
    fun isInvalidToken(req: HttpServletRequest): Boolean {
        val token = req.getHeader(Env.KERIGMA_AUTH)
        return token == null || token != Env.KERIGMA_AUTH_TOKEN
    }

    /**
     * Generador de Token de Session basado en cookie
     *
     * @param username email o identificador unico de cada usuario
     * @param rolId    usado para la lógica de permisos dentro del sistema
     * @param unidad   unidad de tiempo para fijar la vida del token
     * @param cantidad cantidad del tiempo para la vida del token
     */
    fun getTokenCookie(resp: HttpServletResponse, username: String, rolId: Int, unidad: Int, cantidad: Int): String {
        if (!leidas) readKeys()
        try {
            val today = Date()
            val c = Calendar.getInstance()
            c.time = today
            c.add(unidad, cantidad)

            val newDate = c.time
            val algorithm: Algorithm = Algorithm.RSA512(publicKey, privateKey)
            val token: String = JWT.create()
                .withIssuer(Env.KERIGMA_ISSUER)
                .withSubject(username)
                .withClaim("rol", rolId)
                .withIssuedAt(today)
                .withExpiresAt(newDate)
                .sign(algorithm)

            val cookie = Cookie(Env.KERIGMA_AUTH, token)
            cookie.path = "/"
            cookie.isHttpOnly = Env.COOKIE_HTTP_ONLY
            cookie.secure = Env.COOKIE_SECURE
            resp.addCookie(cookie)
            resp.setHeader("Set-Cookie", resp.getHeader("Set-Cookie") + "; SameSite=None")
            return token

        } catch (err: Exception) {
            logger.error(err.message)
        }
        return ""
    }


    /**
     * Validador del token de session
     *
     * @param req   puntero http de intercambio de la peticion actual
     * @param rolId Rol para validacion de los permisos de acceso comparandolo con el tipo del token
     * @return JWT decodificado para su amnipulacion en los controladores
     */
    fun isSecureCookie(req: HttpServletRequest, rolId: Rol): DecodedJWT? {
        if (!leidas) readKeys()
        return try {
            var token = ""
            val cookies = req.cookies
            if (cookies != null) {
                for (cookie in cookies) {
                    if (cookie.name == Env.KERIGMA_AUTH) {
                        token = cookie.value
                        break
                    }
                }
            }
            val algorithm: Algorithm = Algorithm.RSA512(publicKey, privateKey)
            val verifier: JWTVerifier = JWT.require(algorithm)
                .withIssuer(Env.KERIGMA_ISSUER)
                .acceptLeeway(1)
                .build()
            val tokenDecoded: DecodedJWT = verifier.verify(token)

            if (tokenDecoded.getClaim("rol").asInt() >= rolId.value) {
                tokenDecoded
            } else null

        } catch (err: Exception) {
            null
        }
    }

    /**
     * Generador de Token de Session
     *
     * @param username email o identificador unico de cada usuario
     * @param rolId    usado para la lógica de permisos dentro del sistema
     * @param unidad   unidad de tiempo para fijar la vida del token
     * @param cantidad cantidad del tiempo para la vida del token
     */
    fun getToken(username: String, rolId: Int, unidad: Int, cantidad: Int): String {
        if (!leidas) readKeys()
        try {
            val today = Date()
            val c = Calendar.getInstance()
            c.time = today
            c.add(unidad, cantidad)

            val newDate = c.time
            val algorithm: Algorithm = Algorithm.RSA512(publicKey, privateKey)

            return JWT.create()
                .withIssuer(Env.KERIGMA_ISSUER)
                .withSubject(username)
                .withClaim("rol", rolId)
                .withIssuedAt(today)
                .withExpiresAt(newDate)
                .sign(algorithm)

        } catch (err: Exception) {
            logger.error(err.message)
        }
        return ""
    }


    /**
     * Validador del token de session
     *
     * @param req   puntero http de intercambio de la peticion actual
     * @param rolId Rol para validacion de los permisos de acceso comparandolo con el tipo del token
     * @return JWT decodificado para su amnipulacion en los controladores
     */
    fun isSecure(req: HttpServletRequest, rolId: Rol): DecodedJWT? {
        if (!leidas) readKeys()
        return try {
            val token = req.getHeader(Env.KERIGMA_AUTH)
            val algorithm: Algorithm = Algorithm.RSA512(publicKey, privateKey)
            val verifier: JWTVerifier = JWT.require(algorithm)
                .withIssuer(Env.KERIGMA_ISSUER)
                .acceptLeeway(1)
                .build()
            val tokenDecoded: DecodedJWT = verifier.verify(token)

            if (tokenDecoded.getClaim("rol").asInt() >= rolId.value) {
                tokenDecoded
            } else null

        } catch (err: Exception) {
            null
        }
    }


    /**
     * Validador del token de session
     *
     * @param tokens puntero http de intercambio de la peticion actual
     * @param rol    Rol para validacion de los permisos de acceso comparandolo con el tipo del token
     * @return JWT decodificado para su amnipulacion en los controladores
     */
    fun isSecureString(tokens: ArrayList<String?>, rol: Rol): DecodedJWT? {
        if (!leidas) readKeys()
        return try {
            val token = tokens[0]
            val algorithm: Algorithm = Algorithm.RSA512(publicKey, privateKey)
            val verifier: JWTVerifier = JWT.require(algorithm)
                .withIssuer(Env.KERIGMA_ISSUER)
                .acceptLeeway(1)
                .build()
            val tokenDecoded: DecodedJWT = verifier.verify(token)

            if (tokenDecoded.getClaim("rol").asInt() >= rol.value) {
                tokenDecoded
            } else null

        } catch (err: Exception) {
            null
        }
    }

    @Synchronized
    private fun readKeys() {
        if (leidas) return
        try {
            publicKey = Lector.getPublicKey(Env.KERIGMA_PUBLIC_KEY_PATH) as RSAPublicKey?
            privateKey = Lector.getPrivateKey(Env.KERIGMA_PRIVATE_KEY_PATH) as RSAPrivateKey?
            leidas = true
        } catch (err: Exception) {
            logger.error(err.message)
        }
    }
}