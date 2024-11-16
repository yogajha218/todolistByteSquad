package com.example.TodoList.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.TodoList.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task ORDER BY taskImportance ASC")
    fun getTaskOrderByPriority(): Flow<List<Task>>

//    @Query("SELECT * FROM task ORDER BY dueDate DESC")
//    fun getTaskOrderByDueDate(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY title ASC")
    fun getTaskOrderByTitle(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id = :taskId")
    fun getTaskById(taskId: Int): Flow<Task>

    @Update
    suspend fun updateTask(task: Task)

}