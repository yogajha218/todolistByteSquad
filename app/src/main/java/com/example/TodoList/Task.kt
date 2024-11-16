package com.example.TodoList

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
//    val isCompleted: Boolean,
    val taskImportance: Int,
//    val dueDate: Timestamp,
//    val createdAt: Timestamp,
//    val updatedAt: Timestamp,

)
