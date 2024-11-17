package com.example.TodoList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TaskScreen(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    navController: NavController
){
    val startColor = Color(0xFFFF8D23)
    val endColor = Color(0xFFFFAC89)
    val gradientBrush = Brush.linearGradient(
        colors = listOf(startColor, endColor)
    )
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_task_screen")
            }, shape = RoundedCornerShape(8.dp)
                , modifier = Modifier.padding(12.dp)) {
                Row(modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {
                    Icon(imageVector = Icons.Default.Add,
                        contentDescription = "Add task")
                    Text(text = "Add Task")
                }

            }
        }
    ) {
        paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush),
//            verticalArrangement = Arrangement.SpaceBy
        ) {
         item {
             Text(
                 text = "Home",
                 textAlign = TextAlign.Center,
                 fontSize = 28.sp,
                 fontWeight = FontWeight.Bold,
                 color = Color.White,
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(12.dp)
             )
         }
         item {
             Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .horizontalScroll(rememberScrollState()),
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 SortType.values().forEach { sortType ->
                     Row(
                         modifier = Modifier
                             .clickable {
                                 onEvent(TaskEvent.SortTask(sortType))
                             },
                         verticalAlignment = Alignment.CenterVertically
                     ) {
                         RadioButton(
                             selected = state.sortType == sortType,
                             onClick = {onEvent(TaskEvent.SortTask(sortType))},
                         )
                         Text(text = sortType.name)
                     }
                 }
             }
         }
            items(state.tasks){ task ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 6.dp),
//
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    border = BorderStroke(1.dp,
                        Color.Gray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(
                            horizontal = 12.dp,
                            vertical = 10.dp
                        )
                    ){
                        Column(modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = task.title,
                                fontSize = 24.sp,
                                color = Color.Black,
                                lineHeight = 24.sp,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )

                            Text(
                                modifier = Modifier.padding(vertical = 2.dp),
                                text = task.description,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Color.LightGray)
                                .padding(horizontal = 2.dp))
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Absolute.Center
                            ) {
                                Text(text="Today")
                                Text(
                                    text = "06.00 PM - 08.00 PM",
                                    modifier = Modifier.padding(horizontal = 6.dp),
                                    fontSize = 14.sp
                                )
                                Text(text = task.taskImportance.toString() ,modifier = Modifier.background(when (task.taskImportance) {
                                    1 -> Color(0xFFFFC3C3)
                                    2 -> Color(0xFFFFEAB4)
                                    3 -> Color(0xFFC4FFB6)
                                    else -> Color.Gray // Default color if importance is not 1, 2, or 3
                                }, shape = RoundedCornerShape(30.dp)).padding(horizontal = 10.dp), fontSize = 12.sp , color = when(task.taskImportance){
                                    1 -> Color(0xFF9D4040)
                                    2 -> Color(0xFFC0972C)
                                    3 -> Color(0xFF529D40)
                                    else -> Color.Gray
                                }, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(24.dp))
                                IconButton(onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("editableTask", task)
                                    navController.navigate("add_task_screen")
                                }, modifier = Modifier.wrapContentSize().size(40.dp)) {
                                    Image(painter = painterResource(id = R.drawable.edit_icon)
                                        , contentDescription = "edit",
                                        modifier = Modifier.size(36.dp))
                                }
                                IconButton(onClick = {onEvent(TaskEvent.DeleteTask(task))}, modifier = Modifier.wrapContentSize().size(40.dp)) {
                                    Image(painter = painterResource(id = R.drawable.delete_icon)
                                        , contentDescription = "delete",
                                        modifier = Modifier.size(36.dp))
                                }
                            }
                        }


                    }
                }

            }
        }
    }
}

