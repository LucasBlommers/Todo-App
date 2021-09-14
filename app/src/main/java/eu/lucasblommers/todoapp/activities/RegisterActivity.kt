package eu.lucasblommers.todoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.todoapp.R
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import eu.lucasblommers.todoapp.utilities.Utility
import org.json.JSONObject

private val utility = Utility()

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            println("Validating form")
            val etRegisterEmail = findViewById<EditText>(R.id.etRegisterEmail)
            val etRegisterPassword = findViewById<EditText>(R.id.etRegisterPassword)
            val etRegisterDoublePassword = findViewById<EditText>(R.id.etRegisteDoublePassword)

            val email = etRegisterEmail.text.toString().trim()
            if(email == ""){
                println("Empty E-mail")
                utility.makeToast(applicationContext, getString(R.string.empty_email))
                return@setOnClickListener
            }

            if (etRegisterPassword.text.toString().trim() == ""){
                return@setOnClickListener utility.makeToast(this@RegisterActivity, getString(R.string.empty_password))
            }

            if (etRegisterDoublePassword.text.toString().trim() == ""){
                return@setOnClickListener utility.makeToast(this@RegisterActivity, getString(R.string.empty_password))
            }

            if (etRegisterPassword.text.toString() != etRegisterDoublePassword.text.toString()){
                return@setOnClickListener utility.makeToast(this@RegisterActivity, getString(R.string.no_password_match))
            }

            var userJSON = JSONObject()
            userJSON.put("email", etRegisterEmail.text.toString())
            userJSON.put("password", etRegisterPassword.text.toString())
            println("Registering")
            val registerAsync = "http://172.20.11.88:4050/user"
                .httpPost()
                .jsonBody(userJSON.toString())
                .responseString{request, response, result ->
                    when(result){
                        is Result.Failure -> {
                            val ex = result.getException()
                            println("Register ERROR: $ex")
                            utility.makeToast(applicationContext, getString(R.string.login_failed))
                        }

                        is Result.Success -> {
                            println("Register SUCCESS")
                            finishRegister()
                        }
                    }
                }
            registerAsync.join()
        }
    }

    fun finishRegister(){
        finish()
    }
}