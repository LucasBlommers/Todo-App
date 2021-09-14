package eu.lucasblommers.todoapp.rvadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import eu.lucasblommers.todoapp.models.TodoCollection

class TodoCollectionAdapter(private var todoCollections: MutableList<TodoCollection>):
    RecyclerView.Adapter<TodoCollectionAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_todo_collection, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoCollection = todoCollections[position]

        holder.btnCollection.text = todoCollection.title
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