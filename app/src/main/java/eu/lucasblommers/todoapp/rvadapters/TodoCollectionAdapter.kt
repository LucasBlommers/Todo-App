package eu.lucasblommers.todoapp.rvadapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import eu.lucasblommers.todoapp.activities.TodosActivity
import eu.lucasblommers.todoapp.models.TodoCollection

class TodoCollectionAdapter(private var todoCollections: MutableList<TodoCollection>, private var token:String):
    RecyclerView.Adapter<TodoCollectionAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_todo_collection, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoCollection = todoCollections[position]

        holder.btnCollection.text = todoCollection.title

        holder.btnCollection.setOnClickListener {
            val intent = Intent(it.context, TodosActivity::class.java)
            val bundle = Bundle()
            bundle.putString("collectionId", todoCollections[position]._id)
            bundle.putString("token", token)
            startActivity(it.context, intent, bundle)
        }
    }

    override fun getItemCount(): Int {
        return todoCollections.size
    }

    fun loadCollections(collections: MutableList<TodoCollection>){
        todoCollections = collections
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        val btnCollection: Button = itemView.findViewById(R.id.btnTodoCollection)
    }
}