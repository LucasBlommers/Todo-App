package eu.lucasblommers.todoapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import eu.lucasblommers.todoapp.models.StayLoggedin

class StayLoggedinDBHelper {

    fun saveStayLoggedIn(context: Context, stayLoggedIn: StayLoggedin){
        val dbHelper = DBHelper(context, null)
        val db = dbHelper.writableDatabase
        val values = ContentValues()

        values.put(dbHelper.getColToken(), stayLoggedIn.token)

        db.insert(dbHelper.getTableStayLoggedin(), null, values)
    }

    @SuppressLint("Range")
    fun loadStayLoggedIn(context: Context): StayLoggedin?{
        val dbHelper = DBHelper(context, null)
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${dbHelper.getTableStayLoggedin()}"

        val cursor = db.query(dbHelper.getTableStayLoggedin(), null, null, null, null, null, null)

        return if(cursor.moveToFirst()){
            val id:Int = cursor.getInt(cursor.getColumnIndex(dbHelper.getColID()))
            val token = cursor.getString(cursor.getColumnIndex(dbHelper.getColToken()))

            StayLoggedin(id, token)
        }else{
            null
        }
    }

    fun removeStayLoggedin(context: Context, id: Int){
        val dbHelper = DBHelper(context, null)
        val db = dbHelper.writableDatabase

        val deleteQuery = "DELETE FROM ${dbHelper.getTableStayLoggedin()} WHERE ${dbHelper.getColID()} = ${id}"
        db.execSQL(deleteQuery)
    }
}