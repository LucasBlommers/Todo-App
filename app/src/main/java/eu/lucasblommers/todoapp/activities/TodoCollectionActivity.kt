package eu.lucasblommers.todoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.BuildConfig
import com.example.todoapp.R
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import eu.lucasblommers.todoapp.database.StayLoggedinDBHelper
import eu.lucasblommers.todoapp.models.StayLoggedin
import eu.lucasblommers.todoapp.models.TodoCollection
import eu.lucasblommers.todoapp.rvadapters.TodoCollectionAdapter
import kotlinx.coroutines.GlobalScope
import org.json.JSONArray
import org.json.JSONObject

class TodoCollectionActivity : AppCompatActivity() {

    private var token:String? = null

    var rvTodoCollections:RecyclerView? = null
    var todoCollectionAdapter:TodoCollectionAdapter? = null

    private val createTodoCollection = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        todoCollectionAdapter!!.loadCollections(loadTodoCollections())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_collections)

        val extras = intent.extras
        if(extras != null){
            token = extras.getString("token")
        }else{
            //Load the token from the database
            val dbHelper = StayLoggedinDBHelper()
            val stayLoggedin:StayLoggedin = dbHelper.loadStayLoggedIn(this)!!
            token = stayLoggedin.token
        }

        GlobalScope.run {
            //Load TodoCollections
            val todoCollections = loadTodoCollections()
            rvTodoCollections = findViewById<RecyclerView>(R.id.rvTodoCollections)
            todoCollectionAdapter = TodoCollectionAdapter(todoCollections!!, token!!)

            rvTodoCollections!!.layoutManager = LinearLayoutManager(this@TodoCollectionActivity)
            rvTodoCollections!!.adapter = todoCollectionAdapter
        }

        GlobalScope.run {
            val btnCreateCollection = findViewById<Button>(R.id.btnCreateCollectionActivity)

            btnCreateCollection.setOnClickListener {
                val intent = Intent(this@TodoCollectionActivity, CreateTodoCollectionActivity::class.java).apply {
                    putExtra("token", token!!)
                }
                createTodoCollection.launch(intent)
            }
        }
    }

    private fun loadTodoCollections():MutableList<TodoCollection>{

        var todoCollections:MutableList<TodoCollection> = mutableListOf()

        val loadCollectionAsync = "${BuildConfig.rest_url}/taskCollections"
            .httpGet()
            .header("token", token!!)
            .responseString{request, response, result ->
                when(result){
                    is Result.Failure -> {
                        val ex = result.getException()
                        println("FAIL: " + ex.localizedMessage)
                        this.runOnUiThread(Runnable {
                            Toast.makeText(this, getString(R.string.collections_failed), Toast.LENGTH_LONG).show()
                        })
                    }
                    is Result.Success ->{
                        val jsonArray = JSONArray(result.value)
                        println("JSON ARRAY: $jsonArray")
                        //Convert the json array
                        for(i in 0 until jsonArray.length()){
                            val jsonTodoCollection:JSONObject = jsonArray.getJSONObject(i)
                            val jsonTaskList = jsonTodoCollection.getJSONArray("taskList")
                            var taskList = arrayListOf<String>()

                            for(i in 0 until jsonTaskList.length()){
                                taskList.add(jsonTaskList.getString(i))
                            }

                            val todoCollection = TodoCollection(jsonTodoCollection.getString("_id"), jsonTodoCollection.getString("title"), taskList, jsonTodoCollection.getString("owner"))
                            todoCollections.add(todoCollection)
                            println("TODO COLLECTION: $jsonTodoCollection")
                        }
                    }
                }
            }

        loadCollectionAsync.join()
        return todoCollections
    }
}