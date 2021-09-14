package eu.lucasblommers.todoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import com.example.todoapp.R
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import eu.lucasblommers.todoapp.utilities.Utility
import kotlinx.coroutines.GlobalScope
import org.json.JSONObject

class CreateTodoCollectionActivity : AppCompatActivity() {

    private val utility = Utility()
    private var token:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo_collection)

        token = intent.extras!!.getString("token")

        GlobalScope.run {
            val btnCreateCollection = findViewById<Button>(R.id.btnCreateCollection)

            btnCreateCollection.setOnClickListener {
                val etCollectionTitle = findViewById<EditText>(R.id.etCollectionTitle)

                val jsonObject = JSONObject()
                jsonObject.put("title", etCollectionTitle.text.toString())

                val createTodoCollectionAsync = "http://172.20.11.88:4050/taskCollection"
                    .httpPost()
                    .header("token", token!!)
                    .jsonBody(jsonObject.toString())
                    .responseString{request, response, result ->
                        when(result){
                            is Result.Failure -> {
                                val ex = result.getException()
                                println("CREATE TASKCOLLECTION ERROR: ${ex.localizedMessage}")
                                this@CreateTodoCollectionActivity.runOnUiThread(Runnable {
                                    utility.makeToast(this@CreateTodoCollectionActivity, getString(R.string.create_todo_collection_failed))
                                })
                            }
                            is Result.Success -> {
                                finishCreating()
                            }
                        }
                    }
                createTodoCollectionAsync.join()
            }
        }
    }

    fun finishCreating(){
        println("Finishing creating collection")
        finish()
    }
}