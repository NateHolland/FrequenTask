package com.nate.frequentask.themelist

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
import com.nate.frequentask.R
import com.nate.frequentask.data.Theme

@Composable
fun AddThemeDialog(
    onAddTheme: (Theme) -> Unit,
    onDismiss: () -> Unit
) {
    var themeName by remember { mutableStateOf(TextFieldValue("")) }

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
                        text = stringResource(id = R.string.add_theme),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = themeName,
                        onValueChange = { themeName = it },
                        placeholder = { Text(text = stringResource(id = R.string.theme_name)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                val newTheme = Theme(
                                    name = themeName.text,
                                    active = true,
                                    tasks = emptyList() // Initialize with an empty task list
                                )
                                onAddTheme(newTheme)
                                onDismiss()
                            },
                            modifier = Modifier.width(140.dp)
                        ) {
                            Text(text = stringResource(id = R.string.add_theme))
                        }
                    }
                }
            }

        }
    )
}
