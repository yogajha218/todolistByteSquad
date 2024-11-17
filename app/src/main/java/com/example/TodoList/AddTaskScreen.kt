@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.TodoList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@Composable

fun AddTaskScreen(
    navController: NavController,
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val inputColor = Color(0xFFFFE4AD)
    val inputFocusColor = Color(0xFFFF9136)
    var expanded by rememberSaveable { mutableStateOf(false) }
    val items = listOf(1, 2, 3) // Priority levels
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = if (isFocused) inputFocusColor else inputColor
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row( horizontalArrangement = Arrangement.Absolute.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        IconButton(onClick = {navController.navigate("Task_Screen")},
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back Arrow",
                                modifier = Modifier.size(40.dp))
                        }
                        Text("Add Task")
                    }


                }
            )
        },
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            // Title input
            TextField(
                label = {Text(text = "Title")},
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    containerColor = Color.Transparent,
                ),
                value = state.title
                onValueChange = { onEvent(TaskEvent.SetTitle(it)) },
                placeholder = { Text(text = "Write The Title Here ....") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)
                    .background(color = inputColor, shape = RoundedCornerShape(12.dp))
                    .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp)).onFocusChanged {
                        focusState ->   isFocused = focusState.isFocused
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description input
            TextField(
                label = { Text(text = "Description") },
                value = state.description
                onValueChange = { onEvent(TaskEvent.SetDescription(it)) },
                placeholder = { Text(text = "Write Down The Description") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).border(shape = RoundedCornerShape(12.dp), width = 2.dp, color = inputColor)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Priority Dropdown
            Column(
                modifier = Modifier

                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

            ) {
                Box{
                    Text(
                        text = "Priority: ${state.taskImportance}",
                        modifier = Modifier
                            .clickable { expanded = true }
                            .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.border(shape = RoundedCornerShape(16.dp),
                            width = 2.dp, color = Color.Black)
                            .background(shape = RoundedCornerShape(16.dp),
                                color = Color.Transparent)
                    ) {
                        items.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item.toString(), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                                onClick = {
                                    onEvent(TaskEvent.SetTaskImportance(item))
                                    // Update importance
                                    expanded = false
                                },
//                            colors = ,
                                contentPadding = PaddingValues(4.dp)
                            )
                        }
                    }
                }
                Button(
                    onClick = {onEvent(TaskEvent.SaveTask)
                       navController.navigate("task_Screen") // Navigate back after save
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier =Modifier.align(Alignment.End)
                ) { Text(text = "Save Task")}

            }
            // Time Picker Dialog
            if (showTimePicker) {
                DialWithDialog(
                    onConfirm = { timePickerState ->
                        // Handle the confirmed time here
                        val hour = timePickerState.hour
                        val minute = timePickerState.minute
                        val toMillis  = timeToMillis(hour, minute)
                        onEvent(TaskEvent.SetDueTime(toMillis))
                        selectedTime = String.format("%02d:%02d", hour, minute)
                        showTimePicker = false // Close the dialog
                    },
                    onDismiss = {
                        showTimePicker = false // Close the dialog
                    }
                )
            }

        }
    }

}
fun timeToMillis(hour: Int, minute: Int): Long{
    val currentDate = LocalDate.now()
    val localTime = LocalTime.of(hour, minute)
    return localTime.atDate(currentDate).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}