package com.coopera.todoappproject.data.repository

import androidx.lifecycle.LiveData
import com.coopera.todoappproject.data.ToDoDao
import com.coopera.todoappproject.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }
}