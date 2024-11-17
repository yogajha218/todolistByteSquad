package com.example.TodoList

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    viewModel: TaskViewModel,
    state: TaskState,
    onEvent: (TaskEvent) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "task_screen"
    ) {
        // Task List Screen
        composable("task_screen") {
            TaskScreen(
                state = state,
                onEvent = { event ->
                    when (event) {
                        is TaskEvent.ShowDialog -> {
                            // Navigate to AddTaskScreen for a new task
                            navController.navigate("add_task_screen")
                        }
                        is TaskEvent.EditTask -> {
                            // Pass the selected task to AddTaskScreen for editing
                            navController.currentBackStackEntry?.savedStateHandle?.set("editableTask", event.task)
                            navController.navigate("add_task_screen")
                        }
                        else -> viewModel.onEvent(event)
                    }
                },
                navController = navController
            )
        }
        // Add/Edit Task Screen
        composable("add_task_screen")
            AddTaskScreen(
                navController = navController,
                state = state,
                onEvent = onEvent,
            )
        }
    }
}
