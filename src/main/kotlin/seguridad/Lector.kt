/**
 * Copyright 2020 Leito. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package seguridad

import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec


object Lector {

    @Throws(Exception::class)
    fun getPublicKey(filename: String): PublicKey {
        val keyBytes = Files.readAllBytes(Paths.get(filename))
        val spec = X509EncodedKeySpec(keyBytes)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePublic(spec)
    }

    @Throws(Exception::class)
    fun getPrivateKey(filename: String): PrivateKey {
        val keyBytes = Files.readAllBytes(Paths.get(filename))
        val spec = PKCS8EncodedKeySpec(keyBytes)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePrivate(spec)
    }
}