package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import kotlinx.coroutines.GlobalScope
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    val utility = Utility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        GlobalScope.run {
            val btnRegister = findViewById<Button>(R.id.btnLoginRegister)

            btnRegister.setOnClickListener {
                val registerIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(registerIntent)
            }
        }


            val btnLogin = findViewById<Button>(R.id.btnLogin)

            btnLogin.setOnClickListener {
                val etEmail = findViewById<EditText>(R.id.etLoginEmail)
                val etPassword = findViewById<EditText>(R.id.etLoginPassword)

                var json = JSONObject()
                json.put("email", etEmail.text)
                json.put("password", etPassword.text)

                val loginAsync = "http://172.20.11.88:4050/user/login"
                    .httpPost()
                    .jsonBody(json.toString())
                    .responseString{request, response, result ->
                        when(result){
                            is Result.Failure -> {
                                val ex = result.getException()
                                println("LOGIN ERROR: $ex")
                                utility.makeToast(applicationContext, getString(R.string.login_failed))
                            }

                            is Result.Success -> {
                                val data = JSONObject(result.value)

                                val token: String = data.getString("token")
                                val user: JSONObject = data.getJSONObject("user")

                                val intent = Intent(this@LoginActivity, TodoActivity::class.java).apply {
                                    putExtra("token", token)
                                    putExtra("user", user.toString())
                                }
                                startActivity(intent)
                            }
                        }
                    }
                loginAsync.join()
            }
    }
}