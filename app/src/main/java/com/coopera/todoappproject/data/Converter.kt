package com.coopera.todoappproject.data

import androidx.room.TypeConverter
import com.coopera.todoappproject.data.models.Priority

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String ): Priority {
        return Priority.valueOf(priority)
    }

}