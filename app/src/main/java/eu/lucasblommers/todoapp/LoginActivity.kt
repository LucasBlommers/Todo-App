package eu.lucasblommers.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.todoapp.R
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import eu.lucasblommers.todoapp.database.StayLoggedinDBHelper
import eu.lucasblommers.todoapp.models.StayLoggedin
import kotlinx.coroutines.GlobalScope
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private val utility = Utility()
    private var stayLoggedIn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        GlobalScope.run {
            //Check stayLoggedin
            val stayLoggedinDBHelper = StayLoggedinDBHelper()
            val stayLoggedinDBO = stayLoggedinDBHelper.loadStayLoggedIn(this@LoginActivity)

            if(stayLoggedinDBO != null){
                val intent = Intent(this@LoginActivity, TodoActivity::class.java)
                startActivity(intent)
            }
        }


        GlobalScope.run {
            val btnRegister = findViewById<Button>(R.id.btnLoginRegister)

            btnRegister.setOnClickListener {
                val registerIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(registerIntent)
            }
        }

        GlobalScope.run {
            val cbStayLoggedin = findViewById<CheckBox>(R.id.cbStayLoggedIn)

            cbStayLoggedin.setOnCheckedChangeListener { compoundButton, b ->
                stayLoggedIn = compoundButton.isChecked
            }
        }

        GlobalScope.run {
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

                                if(stayLoggedIn){
                                    //Save the token in the database
                                    val stayLoggedin = StayLoggedin(null, token)
                                    val stayLoggedinDBHelper = StayLoggedinDBHelper()

                                    stayLoggedinDBHelper.saveStayLoggedIn(this@LoginActivity, stayLoggedin)
                                }else{
                                    //Check if a loggedin item exists
                                    val dbHelper = StayLoggedinDBHelper()
                                    val stayLoggedinDBO: StayLoggedin? = dbHelper.loadStayLoggedIn(this@LoginActivity)
                                    //If loggedin item exists remove it
                                    if(stayLoggedinDBO != null){
                                        dbHelper.removeStayLoggedin(this@LoginActivity, stayLoggedinDBO.id!!)
                                    }
                                }

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
}