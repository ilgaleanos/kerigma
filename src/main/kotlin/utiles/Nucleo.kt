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

package utiles

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import utils.Env

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


class Nucleo {
    // activar desactivar los cors
    private val ENABLE_CORS = true

    // strings para header en response
    private val TYPE_JSON = "application/json;charset=utf-8"
    private val TYPE_TEXT = "text/plain;charset=utf-8"
    private val TYPE_HTML = "text/html;charset=utf-8"
    private val TYPE_CSV = "application/csv;charset=ISO-8859-1"
    private val gson: Gson = Gson()

    // tipo de transformacion generica de manipulacion de json para parseo con gson
    val typeMapStringObject: Type = object : TypeToken<HashMap<String?, Any?>?>() {}.getType()

    // tipo de transformacion generica de manipulacion de json para parseo con gson para listas
    val typeList: Type = object : TypeToken<MutableList<String?>?>() {}.getType()


    /**
     * Escribe en el response los headers necesarios para permitir CORS
     *
     * @param resp el actual puntero de intercambio http
     */
    private fun corsHeaders(resp: HttpServletResponse) {
        resp.setHeader("Access-Control-Allow-Origin", Env.HOST_FRONT)
        resp.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, withCredentials")
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE")
        resp.setHeader("Access-Control-Allow-Credentials", "true")
    }

    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp     el actual puntero de intercambio http
     * @param response cadena final de entrega en el intercambio http
     */
    fun sendTEXT(resp: HttpServletResponse, response: String) {
        if (ENABLE_CORS) corsHeaders(resp)
        resp.contentType = TYPE_TEXT
        val out = resp.writer
        out.println(response)
    }

    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp     el actual puntero de intercambio http
     * @param response cadena final de entrega en el intercambio http
     */
    @Throws(IOException::class)
    fun sendHTML(resp: HttpServletResponse, response: String) {
        if (ENABLE_CORS) corsHeaders(resp)
        resp.contentType = TYPE_HTML
        val out = resp.writer
        out.println(response)
    }

    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp     el actual puntero de intercambio http
     * @param response JsonObject final con los datos de entrega en el intercambio http
     */
    @Throws(IOException::class)
    fun sendJSON(resp: HttpServletResponse, response: JsonObject) {
        if (ENABLE_CORS) corsHeaders(resp)
        resp.contentType = TYPE_JSON
        val out = resp.writer
        out.println(response.toString())
        out.close()
    }

    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp     el actual puntero de intercambio http
     * @param response JsonObject final con los datos de entrega en el intercambio http
     */
    @Throws(IOException::class)
    fun sendCSV(resp: HttpServletResponse, response: String) {
        if (ENABLE_CORS) corsHeaders(resp)
        resp.contentType = TYPE_CSV
        val out = resp.writer
        out.println(response)
    }

    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp el actual puntero de intercambio http
     */
    @Throws(IOException::class)
    fun sendErrorJSON(resp: HttpServletResponse) {
        if (ENABLE_CORS) corsHeaders(resp)
        resp.status = Codigos.INTERNAL_SERVER_ERROR
        resp.contentType = TYPE_JSON
        val out = resp.writer
        val response = JsonObject()
        response.addProperty("success", 0)
        response.addProperty("data", "internal server error")
        out.println(response.toString())
    }

    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp el actual puntero de intercambio http
     */
    @Throws(IOException::class)
    fun sendBadRequestJSON(resp: HttpServletResponse, params: Array<String>) {
        if (ENABLE_CORS) corsHeaders(resp)
        resp.status = Codigos.BAD_REQUEST
        resp.contentType = TYPE_JSON
        val out = resp.writer
        val response = JsonObject()
        response.addProperty("success", 0)
        response.addProperty("message", Arrays.toString(params) + " are required")
        out.println(response.toString())
    }

    /**
     * Envía respuesta de método no permitido
     *
     * @param resp HttpServletResponse
     */
    @Throws(IOException::class)
    fun sendInvalidMethod(resp: HttpServletResponse) {
        if (ENABLE_CORS) corsHeaders(resp)
        resp.contentType = TYPE_TEXT
        val out = resp.writer
        out.println("This method is not allowed")
    }

    /**
     * Finaliza el intercambio HTTP con el string proporcionado y lo escribe en el response
     *
     * @param resp el actual puntero de intercambio http
     */
    @Throws(IOException::class)
    fun sendForbiddenTEXT(resp: HttpServletResponse) {
        if (ENABLE_CORS) corsHeaders(resp)
        resp.status = Codigos.FORBIDDEN
        resp.contentType = TYPE_TEXT
        val out = resp.writer
        out.println("FORBIDDEN")
    }

    /**
     * Headers que proporcionan seguridad adicional en html
     * https://www.owasp.org/index.php/OWASP_Secure_Headers_Project#tab=Headers
     *
     * @param resp el actual puntero de intercambio http
     */
    private fun securesHeaders(resp: HttpServletResponse) {
        resp.setHeader("X-Frame-Options", "DENY")
        resp.setHeader("X-XSS-Protection", "1;mode=block")
        resp.setHeader("X-Content-Type-Options", "nosniff")
        resp.setHeader("Connection", "close")
        resp.setHeader("cache-control", "private, no-store")
    }

    /**
     * Valida que los campos ingresados por parámetros de urlEncoded sean los requeridos
     *
     * @param req  HttpServletRequest
     * @param data String[]
     * @return boolean
     */
    @Throws(UnsupportedEncodingException::class)
    fun isInvalidUrlEncoded(req: HttpServletRequest, data: Array<String?>): Boolean {
        req.characterEncoding = "UTF-8"
        var value: String? = null
        for (key in data) {
            value = req.getParameter(key)
            if (value == null) {
                return true
            }
        }
        return false
    }


    /**
     * Valida que todos los parametros pasados por url en la peticion run existan
     *
     * @param queries HashMap<String></String>, String>
     * @param data    String[]
     * @return boolean
     */
    fun isInvalidQuery(queries: HashMap<String, ArrayList<String>>, data: Array<String?>): Boolean {
        for (key in data) {
            if (!queries.containsKey(key)) {
                return true
            }
        }
        return false
    }


    /**
     * @param req
     * @return
     */
    @Throws(UnsupportedEncodingException::class)
    fun urlEncodedToJson(req: HttpServletRequest): JsonObject? {
        req.characterEncoding = "UTF-8"
        val body = JsonObject()
        for (key in req.parameterMap.keys) {
            body.addProperty(key, req.getParameter(key))
        }
        return body
    }


    /**
     * Mapea los valores del query contenidos en el respuesta a un HashMap
     *
     * @param req HttpServletRequest
     * @return mapeo HashMap<String></String>,String>
     */
    @Throws(UnsupportedEncodingException::class)
    fun getQueries(req: HttpServletRequest): HashMap<String, ArrayList<String>?>? {
        req.characterEncoding = "UTF-8"
        val mapeo = HashMap<String, ArrayList<String>?>()
        var cadena: String? = req.queryString ?: return mapeo
        cadena = URLDecoder.decode(cadena, "UTF-8")
        for (par in cadena.split("&").toTypedArray()) {
            val values = par.split("=").toTypedArray()
            if (!mapeo.containsKey(values[0])) {
                mapeo[values[0]] = ArrayList()
            }
            mapeo[values[0]]!!.add(values[1])
        }
        return mapeo
    }


    /**
     * Mapea los valores del query contenidos en el respuesta a un HashMap
     *
     * @param req HttpServletRequest
     * @return mapeo HashMap<String></String>,String>
     */
    @Throws(UnsupportedEncodingException::class)
    fun getPreQueries(req: HttpServletRequest): ArrayList<String> {
        req.characterEncoding = "UTF-8"
        val mapeo = ArrayList<String>()
        var cadena: String = req.requestURI ?: return mapeo
        cadena = URLDecoder.decode(cadena, "UTF-8")
        mapeo.addAll(Arrays.asList(*cadena.split("/").toTypedArray()))
        return mapeo
    }


    /**
     * Parsea el cuerpo del pregunta body a un Gson
     *
     * @param req HttpServletRequest
     * @return JsonObject
     * @throws IOException cuando no pueda crear el reader
     */
    @Throws(IOException::class)
    fun getBody(req: HttpServletRequest): JsonObject {
        req.characterEncoding = "UTF-8"
        return try {
            JsonParser.parseReader(req.reader).getAsJsonObject()
        } catch (err: Exception) {
            JsonObject()
        }
    }


    /**
     * Valida que los campos ingresados por parámetros de body sean los requeridos
     *
     * @param body JsonObject
     * @param data String[]
     * @return boolean
     */
    fun isInvalidBody(body: JsonObject, data: Array<String>): Boolean {
        for (key in data) {
            if (!body.has(key)) {
                return true
            }
        }
        return false
    }

}