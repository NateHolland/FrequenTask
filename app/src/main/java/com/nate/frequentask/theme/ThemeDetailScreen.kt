package com.nate.frequentask.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nate.frequentask.data.ThemeRepository
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeDetailScreen(
    navController: NavController,
    themeID: String,
    themeRepository: ThemeRepository
) {
    var isEditing by remember { mutableStateOf(false) }
    var isAddTaskDialogVisible by remember { mutableStateOf(false) }
    val themeList by themeRepository.themesList.observeAsState()
    val theme = themeList!!.find { it.id == themeID }!!
    var themeName by remember { mutableStateOf(theme.name) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isEditing) {
                        BasicTextField(
                            value = themeName,
                            onValueChange = { themeName = it },
                            textStyle = TextStyle.Default.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } else {
                        Text(text = themeName)
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(
                            onClick = { isEditing = true }
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                        }
                    } else {
                        IconButton(
                            onClick = {
                                isEditing = false
                                themeRepository.updateThemeName(themeID, themeName)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                    IconButton(
                        onClick = { isAddTaskDialogVisible = true }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            )
        },
        content = { padding ->
            if (isAddTaskDialogVisible) {
                AddTaskDialog(
                    onAddTask = { newTask ->
                        themeRepository.addTask(themeID, newTask)
                        isAddTaskDialogVisible = false
                    },
                    onDismiss = { isAddTaskDialogVisible = false }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(theme.tasks.sortedBy { it.nextDueOn }.reversed()) { task ->
                        TaskListItem(
                            task = task,
                            onTaskClick = { navController.navigate("taskDetail/${theme.id}/${it}") },
                            onTaskDelete = { themeRepository.deleteTask(themeID, it) }
                        )
                    }
                }
            }
        }
    )
}
