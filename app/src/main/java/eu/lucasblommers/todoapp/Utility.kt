package eu.lucasblommers.todoapp

import android.content.Context
import android.widget.Toast

class Utility {
    fun makeToast(context: Context, text: String){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}