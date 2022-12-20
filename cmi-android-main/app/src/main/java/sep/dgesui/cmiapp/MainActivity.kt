package sep.dgesui.cmiapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val botonIniciarSesion = findViewById<Button>(R.id.iniciarSesion)
        val queue = Volley.newRequestQueue(this)
        val sharedPreferences = getSharedPreferences("access", MODE_PRIVATE)
        val user: String? = sharedPreferences.getString("user","")
        findViewById<EditText>(R.id.usuario).setText(user)

        botonIniciarSesion.setOnClickListener {
            val usuario = findViewById<EditText>(R.id.usuario).text.toString()
            val contrasena = findViewById<EditText>(R.id.contrasena).text.toString()
            val stringRequest : StringRequest = object : StringRequest(Method.POST,
                "https://dgesui.ses.sep.gob.mx/auth/realms/CMI/protocol/openid-connect/token",
//            val stringRequest: StringRequest = object : StringRequest(Method.POST,
//                "https://dgesui.ses.sep.gob.mx/pruebascmi/auth/realms/CMI/protocol/openid-connect/token",
                { response ->
                    val jsonObject = JSONObject(response)
                    val intent = Intent(this@MainActivity, Home::class.java)
                    intent.putExtra("token", jsonObject.getString("access_token"))
                    intent.putExtra("refreshToken", jsonObject.getString("refresh_token"))
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("user", usuario)
                    editor.apply()
                    startActivity(intent)
                },
                { error ->
                    val toast =
                        Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG)
                    toast.show()
                }) {
                override fun getParams(): MutableMap<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params.put("username", usuario)
                    params.put("password", contrasena)
                    params.put("client_id", "app_cmi")
                    params.put("client_secret", "Password.1")
                    params.put("grant_type", "password")
                    return params
                }
            }
            queue.add(stringRequest)
        }
    }
}