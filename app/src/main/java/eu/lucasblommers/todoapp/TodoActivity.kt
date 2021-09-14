package eu.lucasblommers.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todoapp.R

class TodoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
    }
}