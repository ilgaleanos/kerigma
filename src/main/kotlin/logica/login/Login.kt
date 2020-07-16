package logica.login

import com.auth0.jwt.interfaces.DecodedJWT
import jdk.nashorn.internal.parser.DateParser.DAY
import modelos.Rol
import security.Angel
import java.io.IOException
import java.util.Calendar
import javax.servlet.http.HttpServletRequest


class Login {

    private val miguel: Angel = Angel()

    fun obtenerToken(): String {
        return miguel.getToken("leito", 9, Calendar.HOUR, 1)
    }

    fun validarToken(req: HttpServletRequest): DecodedJWT? {
        return miguel.isSecure(req, Rol.SUPERADMIN)
    }
}