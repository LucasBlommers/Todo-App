package eu.lucasblommers.todoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.BuildConfig
import com.example.todoapp.R
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import eu.lucasblommers.todoapp.models.Todo
import eu.lucasblommers.todoapp.rvadapters.TodosAdapter
import eu.lucasblommers.todoapp.utilities.Utility
import kotlinx.coroutines.GlobalScope
import org.json.JSONArray
import org.json.JSONObject

class TodosActivity : AppCompatActivity() {

    val utility = Utility()

    var collectionId:String? = null
    var rvTodos: RecyclerView? = null
    var todosAdapter:TodosAdapter? = null
    var token:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todos)

        collectionId = intent.getBundleExtra("collectionId").toString()
        token = intent.getBundleExtra("token").toString()
        //Load tasks
        GlobalScope.run{
            val todos = loadTodos()

            rvTodos = findViewById(R.id.rvTodos)
            todosAdapter = TodosAdapter(todos)

            rvTodos!!.layoutManager = LinearLayoutManager(this@TodosActivity)
            rvTodos!!.adapter = todosAdapter
        }

        GlobalScope.run {
            val btnCreateTodo = findViewById<Button>(R.id.btnCreateTodoActivity)

        }
    }

    fun loadTodos(): MutableList<Todo>{
        var todos:MutableList<Todo> = mutableListOf()

        val loadTodosAsync = "${BuildConfig.rest_url}/task"
            .httpGet()
            .header("token", token!!)
            .responseString{request, response, result ->
                when(result){
                    is Result.Failure -> {
                        val ex = result.getException()
                        println("Error loading tasks: ${ex.localizedMessage}")

                        this.runOnUiThread(Runnable {
                            utility.makeToast(this, getString(R.string.loading_todos_failed))
                        })
                    }
                    is Result.Success -> {
                        val todosJsonArray = JSONArray(result.value)

                        //Convert the json array
                        for(i in 0 until todosJsonArray.length()){
                            val todoJson:JSONObject = todosJsonArray.getJSONObject(i)

                            val id = todoJson.getString("_id")
                            val title = todoJson.getString("title")
                            val body = todoJson.getString("body")
                            val collectionId = todoJson.getString("collectionId")
                            val owner = todoJson.getString("owner")

                            todos.add(Todo(id, title, body, collectionId,owner))

                        }
                    }
                }
            }
        loadTodosAsync.join()

        return todos
    }
}