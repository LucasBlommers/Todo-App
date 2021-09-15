package eu.lucasblommers.todoapp.models

data class Todo(var _id:String, var title:String, var body: String, var taskCollectionId:String, var owner: String)
