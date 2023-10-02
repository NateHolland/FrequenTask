package com.nate.frequentask.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nate.frequentask.R
import com.nate.frequentask.data.Theme
import com.nate.frequentask.displayDate

@Composable
fun TaskListItem(
    task: Theme.Task,
    onTaskClick: (String) -> Unit,
    onTaskDelete: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task.id) }
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Task Name
            Text(
                text = task.name,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            // Next Due On
            Text(
                text = formatNextDueOn(task.nextDueOn),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            // Kebab Menu
            IconButton(
                onClick = { isExpanded = !isExpanded }
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        }
        if (isExpanded) {
            OutlinedButton(onClick = {
                onTaskDelete(task.id)
                isExpanded = false
            }, modifier = Modifier.align(Alignment.End)) {
                Text(text = stringResource(id = R.string.delete))
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
private fun formatNextDueOn(timestamp: Long): String {
    return "Next Due On: ${timestamp.displayDate()}"
}
