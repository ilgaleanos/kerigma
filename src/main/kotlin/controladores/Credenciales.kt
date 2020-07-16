package controladores

import com.google.gson.JsonObject
import logica.login.Login
import utiles.Codigos
import utiles.Nucleo
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/login")
class Credenciales : HttpServlet() {

    private val nc: Nucleo = Nucleo()
    val login: Login = Login()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val response = JsonObject()
        response.addProperty("token", login.obtenerToken())
        nc.sendJSON(resp, response)
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        val response = JsonObject()
        val token = login.validarToken(req)
        response.addProperty("valido", (token != null))
        nc.sendJSON(resp, response)
    }

    override fun doOptions(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.status = Codigos.NO_CONTENT
        nc.sendTEXT(resp, "")
    }
}