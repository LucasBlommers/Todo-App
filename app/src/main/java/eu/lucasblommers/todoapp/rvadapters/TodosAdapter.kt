package eu.lucasblommers.todoapp.rvadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import eu.lucasblommers.todoapp.models.Todo

class TodosAdapter (private var todos: MutableList<Todo>):
    RecyclerView.Adapter<TodosAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_todos_todo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todos[position]

        holder.btnTodo.text = todo.title
    }

    override fun getItemCount(): Int {
        return todos.size
    }

        class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
            val btnTodo = itemView.findViewById<Button>(R.id.btnTodo)
        }
}