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
package utils

import ch.qos.logback.classic.Level

/**
 * VARIABLES GLOBALES PARA LOS DIFERENTES ENTORNOS
 *
 *
 * Se deberian poner en variables de entorno pero aun así centralizarlas en una
 * sola clase solo cambia el origen. Aquí puedes directamente ya sea del entorno
 * o definidas aquí.
 */
object Env {
    const val KERIGMA_PSQL_CONNECTIONS = 3
    const val COOKIE_HTTP_ONLY = true
    const val COOKIE_SECURE = false
    const val HOST_FRONT = "*"

    val KERIGMA_AUTH = System.getenv("KERIGMA_AUTH")
    val KERIGMA_ISSUER = System.getenv("KERIGMA_ISSUER")
    val KERIGMA_PUBLIC_KEY_PATH = System.getenv("KERIGMA_PUBLIC_KEY_PATH")
    val KERIGMA_PRIVATE_KEY_PATH = System.getenv("KERIGMA_PRIVATE_KEY_PATH")
    val KERIGMA_AUTH_TOKEN = System.getenv("KERIGMA_AUTH_TOKEN")

    private val KERIGMA_ENVIRONMENT =
        if (System.getenv("KERIGMA_ENVIRONMENT") == null) "" else System.getenv("KERIGMA_ENVIRONMENT")
    var levelLog: Level = if (KERIGMA_ENVIRONMENT == "DEBUG") Level.INFO else Level.ERROR
}