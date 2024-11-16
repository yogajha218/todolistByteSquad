package com.example.TodoList

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val title: String = "",
    val description: String = "",
    val taskImportance: Int = 3,
    val isAddingTask: Boolean = false,
    val sortType: SortType = SortType.TITLE,

)
