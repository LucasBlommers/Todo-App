package eu.lucasblommers.todoapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.GlobalScope

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        GlobalScope.run {
            val query = ("CREATE TABLE IF NOT EXISTS " + TABLE_LOGGEDIN + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TOKEN + " TEXT" + ")")
            db!!.execSQL(query)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun getTableStayLoggedin():String{
        return TABLE_LOGGEDIN
    }

    fun getColID(): String{
        return COL_ID
    }

    fun getColToken(): String{
        return COL_TOKEN
    }

    companion object{
        private val DATABASE_NAME = "TODOAPP"
        private val DATABASE_VERSION = 1

        val TABLE_LOGGEDIN = "loggedin"

        val COL_ID = "id"
        val COL_TOKEN = "token"
    }
}