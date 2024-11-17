package com.example.TodoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.TodoList.data.TaskDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(
    private val dao: TaskDao
): ViewModel() {
   private val _sortType = MutableStateFlow(SortType.TITLE)
    private val _tasks = _sortType
        .flatMapLatest { sortType ->
            when(sortType)
            {
                SortType.TITLE -> dao.getTaskOrderByTitle()
                SortType.TASK_IMPORTANCE -> dao.getTaskOrderByPriority()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
   private val _state = MutableStateFlow(TaskState())
    val state = combine(_state, _sortType, _tasks) { state, sortType, tasks ->
        state.copy(
            tasks = tasks,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())

   fun onEvent(event: TaskEvent){
       when(event){
           is TaskEvent.DeleteTask -> {
                 viewModelScope.launch {
                     dao.deleteTask(event.task)
                 }
           }
           TaskEvent.SaveTask -> {
               val title = state.value.title
               val description = state.value.description
               val taskImportance = state.value.taskImportance
               val dueTime = state.value.dueTime

               if(title.isBlank())
               {
                   return
               }
               val task = Task(
                   title = title,
                   description = description,
                   taskImportance = taskImportance,
                   dueTime = dueTime,
               )
               viewModelScope.launch {
                   dao.insertTask(task)
               }
               _state.update{it.copy(
                   isAddingTask = false,
                    title = "",
                   description = "",
                   taskImportance = 3,
                   dueTime = 0L,
               )}

           }
           is TaskEvent.SetTitle ->{
               _state.update { it.copy(
                   title = event.title
               ) }
           }
           is TaskEvent.SetTaskImportance ->{
               _state.update { it.copy(
                   taskImportance = event.taskImportance
               ) }
           }
           is TaskEvent.SetDescription ->{
               _state.update { it.copy(
                   description = event.description
               ) }
           }
           is TaskEvent.SetDueTime -> {
               _state.update { it.copy(
                   dueTime = event.dueTime
               ) }
           }
           is TaskEvent.SortTask ->{
               _sortType.value = event.sortType
           }
       }
   }
}