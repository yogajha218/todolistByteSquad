package com.example.TodoList

interface TaskEvent {
    object SaveTask: TaskEvent
    data class SetTitle(val title: String): TaskEvent
    data class SetDescription(val description: String): TaskEvent
    data class SetTaskImportance(val taskImportance: Int): TaskEvent
    data class SetDueTime(val dueTime: Long): TaskEvent
    object ShowDialog: TaskEvent
    object HideDialog: TaskEvent
    data class SortTask(val sortType: SortType): TaskEvent
    data class DeleteTask(val task: Task): TaskEvent


}