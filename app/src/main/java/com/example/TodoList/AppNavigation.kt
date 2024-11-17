package com.example.TodoList

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    viewModel: TaskViewModel,
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    scheduler: AlarmScheduler,
    alarmitem: Alarm?
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
                onEvent = onEvent,
                navController = navController
            )
        }

        // Add/Edit Task Screen
        composable("add_task_screen") {
            AddTaskScreen(
                navController = navController,
                state = state,
                onEvent = onEvent, // Pass the task if editing
                scheduler = scheduler,
                alarmitem = alarmitem
            )
        }
    }
}
