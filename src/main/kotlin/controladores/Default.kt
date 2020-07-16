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

package controladores

import utiles.Codigos
import utiles.Nucleo
import java.io.IOException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet("/")
class Default : HttpServlet() {

    private val fw: Nucleo = Nucleo()
    val kerigma = "  _  __         _                       \n" +
            " | |/ /        (_)                      \n" +
            " | ' / ___ _ __ _  __ _ _ __ ___   __ _ \n" +
            " |  < / _ \\ '__| |/ _` | '_ ` _ \\ / _` |\n" +
            " | . \\  __/ |  | | (_| | | | | | | (_| |\n" +
            " |_|\\_\\___|_|  |_|\\__, |_| |_| |_|\\__,_|\n" +
            "                   __/ |                \n" +
            "                  |___/                 "

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        fw.sendTEXT(resp, kerigma)
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        fw.sendTEXT(resp, kerigma)
    }

    override fun doOptions(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.status = Codigos.NO_CONTENT
        fw.sendTEXT(resp, "")
    }
}