package eu.lucasblommers.todoapp.models

data class TodoCollection(var _id:String, var title: String, var taskList:MutableList<String>, var owner: String)
