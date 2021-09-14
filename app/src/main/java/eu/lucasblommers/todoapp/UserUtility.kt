package eu.lucasblommers.todoapp

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.todoapp.R
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPatch
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import org.json.JSONObject

class UserUtility {
    fun validateToken(context:Activity, token:String): Boolean{
        var validated = false
        val jsonToken = JSONObject()
        jsonToken.put("token", token)

        val verifyAsync = "http://172.20.11.88:4050/user/verify"
            .httpPost()
            .jsonBody(jsonToken.toString())
            .responseString{request, response, result ->
                when(result){
                    is Result.Failure -> {
                        val ex = result.getException()
                        context.runOnUiThread(Runnable {
                            Toast.makeText(context, context.getString(R.string.validation_failed), Toast.LENGTH_LONG).show()
                        })
                    }
                    is Result.Success -> {
                        validated = true
                    }
                }
            }
        verifyAsync.join()
        return validated
    }
}