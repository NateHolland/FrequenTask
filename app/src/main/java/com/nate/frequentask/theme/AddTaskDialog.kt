package com.nate.frequentask.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.nate.frequentask.R
import com.nate.frequentask.data.Theme
import com.nate.frequentask.components.InfinteSpinner
import java.util.Date

@Composable
fun AddTaskDialog(
    navController: NavController,
    theme: Theme,
    onAddTask: (Theme.Task) -> Unit,
    onDismiss: () -> Unit
) {
    var taskName by remember { mutableStateOf(TextFieldValue("")) }
    var taskDescription by remember { mutableStateOf(TextFieldValue("")) }
    var taskFrequency by remember { mutableLongStateOf(1L) } // Default frequency is every day
    var maxFrequency by remember { mutableIntStateOf(30) } // Max frequency is every 5 days
    var items by remember {
        mutableStateOf((1..maxFrequency).toList())
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Box(
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.add_task),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        placeholder = { Text(text = stringResource(id = R.string.task_name)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        placeholder = { Text(text = stringResource(id = R.string.task_description)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.frequency),
                        style = MaterialTheme.typography.labelMedium
                    )
                    // Spinner for selecting frequency
                    InfinteSpinner(
                        selectedItem = taskFrequency.toInt(),
                        onValueChange = { taskFrequency = it.toLong() },
                        more = {
                            maxFrequency += 10
                            items = (1..maxFrequency).toList()
                        },
                        items = items
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onDismiss() },
                            modifier = Modifier.width(140.dp)
                        ) {
                            Text(text = stringResource(id = R.string.cancel))
                        }
                        Button(
                            onClick = {
                                val newTask = Theme.Task(
                                    name = taskName.text,
                                    description = taskDescription.text,
                                    frequency = taskFrequency,
                                    nextDueOn = Date().time,
                                    note = ""
                                )
                                onAddTask(newTask)
                                onDismiss()
                            },
                            modifier = Modifier.width(140.dp)
                        ) {
                            Text(text = stringResource(id = R.string.add_task))
                        }
                    }
                }
            }
        }
    )
}
