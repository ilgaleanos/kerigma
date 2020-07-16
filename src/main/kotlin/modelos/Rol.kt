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

package modelos


/**
 * Estandarizamos los Roles del sistema para diferentes niveles de acceso, requerido par JWT
 */
enum class Rol(val value: Int) {
    ROBOT(1),
    API(2),
    EXTERNO(3),
    LOGISTICO(4),
    SUPERVISOR(5),
    ADMINISTRADOR(6),
    SUPERADMIN(9);
}
