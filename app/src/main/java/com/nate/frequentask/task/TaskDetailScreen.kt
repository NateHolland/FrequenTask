package com.nate.frequentask.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nate.frequentask.R
import com.nate.frequentask.components.FrequentTaskDatePickerDialog
import com.nate.frequentask.data.Theme
import com.nate.frequentask.data.updateFrequency
import com.nate.frequentask.data.updateLastCompleted
import com.nate.frequentask.components.InfinteSpinner
import com.nate.frequentask.data.ThemeRepository
import com.nate.frequentask.data.complete
import com.nate.frequentask.displayDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavController,
    theme: Theme,
    task: Theme.Task,
    themeRepository: ThemeRepository
) {
    var isEditingTitle by remember { mutableStateOf(false) }
    var isEditingDescription by remember { mutableStateOf(false) }
    var taskName by remember { mutableStateOf(task.name) }
    var taskDescription by remember { mutableStateOf(task.description) }
    var showNextDueDatePicker by remember { mutableStateOf(false) }
    var showLastCompletedDatePicker by remember { mutableStateOf(false) }
    var showMarkDoneDialog by remember { mutableStateOf(false) }
    var maxFrequency by remember { mutableIntStateOf(task.frequency.toInt() + 5) } // Max frequency is 5 days
    var items by remember {
        mutableStateOf((1..maxFrequency).toList())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isEditingTitle) {
                        BasicTextField(
                            value = taskName,
                            onValueChange = { taskName = it },
                            textStyle = TextStyle.Default.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } else {
                        Text(text = taskName)
                    }
                },
                actions = {
                    if (!isEditingTitle) {
                        IconButton(
                            onClick = { isEditingTitle = true }
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                        }
                    } else {
                        IconButton(
                            onClick = {
                                isEditingTitle = false
                                themeRepository.updateTaskName(theme.id, task, taskName)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (showNextDueDatePicker) {
                    FrequentTaskDatePickerDialog(
                        onDateSelected = {
                            it?.also {
                                themeRepository.updateTask(
                                    theme.id,
                                    task.copy(nextDueOn = it)
                                )
                            }
                        },
                        onDismiss = { showNextDueDatePicker = false }
                    )
                }
                if (showLastCompletedDatePicker) {
                    FrequentTaskDatePickerDialog(
                        onDateSelected = {
                            it?.also {
                                themeRepository.updateTask(theme.id, task.updateLastCompleted(it))
                            }
                        },
                        onDismiss = { showLastCompletedDatePicker = false },
                        allowPastDates = true
                    )
                }
                if(showMarkDoneDialog){
                    ShowMarkDoneDialog(
                        onDismiss = { showMarkDoneDialog = false },
                        onMarkDone = {
                            themeRepository.updateTask(theme.id, task.complete(it))
                            showMarkDoneDialog = false
                        }
                    )
                }
                // Task details
                ElevatedButton(onClick = { showMarkDoneDialog = true }) {
                    Text(stringResource(id = R.string.complete_task))
                }
                Text(text = "Description:", style = MaterialTheme.typography.labelSmall)
                if (isEditingDescription) {
                    val focusRequester = remember { FocusRequester() }
                    TextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        textStyle = TextStyle.Default.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp)
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            isEditingDescription = false
                        })
                    )
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                } else {
                    Text(
                        text = taskDescription,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                isEditingDescription = true
                            },
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = task.note,
                    modifier = Modifier
                        .padding(4.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(6.dp)
                            .weight(1f)
                            .clickable {
                                showLastCompletedDatePicker = true
                            }
                    ) {
                        Text(
                            text = "Last Completed:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = task.lastCompletedOn?.displayDate() ?: "-",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),

                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(6.dp)
                            .weight(1f)
                            .clickable {
                                showNextDueDatePicker = true
                            }
                    ) {
                        Text(
                            text = "Next Due:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = task.nextDueOn.displayDate(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Frequency:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(4.dp)
                )
                // Spinner for selecting frequency
                InfinteSpinner(
                    selectedItem = task.frequency.toInt(),
                    onValueChange = {
                        themeRepository.updateTask(
                            theme.id,
                            task.updateFrequency(it)
                        )
                    },
                    more = {
                        maxFrequency += 10
                        items = (1..maxFrequency).toList()
                    },
                    items = items
                )
            }
        }
    )
}

