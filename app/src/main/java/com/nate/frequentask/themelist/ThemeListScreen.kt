package com.nate.frequentask.themelist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nate.frequentask.R
import com.nate.frequentask.data.Theme
import com.nate.frequentask.data.ThemeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeListScreen(
    navController: NavController,
    themeRepository: ThemeRepository
) {
    var isAddThemeDialogVisible by remember { mutableStateOf(false) }
    val themeList by themeRepository.themesList.observeAsState()
    var expanded: String? by remember { mutableStateOf(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(
                        onClick = { isAddThemeDialogVisible = true }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            )
        },
        content = { padding ->
            if (isAddThemeDialogVisible) {
                AddThemeDialog(
                    onAddTheme = { newTheme ->
                        themeRepository.addTheme(newTheme)
                        isAddThemeDialogVisible = false
                    },
                    onDismiss = { isAddThemeDialogVisible = false }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = padding.calculateTopPadding())
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(themeList?.sortedBy { it.created } ?: emptyList()) { theme ->
                        ThemeListItem(
                            theme = theme,
                            onItemClick = {
                                navController.navigate("themeDetail/${theme.id}")
                            },
                            onThemeDelete = {
                                themeRepository.deleteTheme(it)
                                navController.apply {
                                    val id = currentDestination!!.id
                                    popBackStack(id, true)
                                    navigate(id)

                                }
                            },
                            onSetActive = { active ->
                                themeRepository.setActiveTheme(theme, active)
                            },
                            expanded = expanded,
                            expand = { expanded = it }
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun ThemeListItem(
    theme: Theme,
    onItemClick: () -> Unit,
    onThemeDelete: (Theme) -> Unit,
    onSetActive: (Boolean) -> Unit,
    expanded: String?,
    expand: (String?) -> Unit
) {
    var active by remember { mutableStateOf(theme.active) }
    var isExpanded = expanded == theme.id
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (active) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surfaceDim)
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        Column {
            ListItem(headlineContent = { Text(text = theme.name, modifier = Modifier) },
                leadingContent = {
                    IconButton(
                        onClick = {
                            if (expanded != theme.id) {
                                expand(theme.id)
                            } else {
                                expand(null)
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .clickable { onItemClick() }
                    .background(if (active) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surfaceDim),
                colors = ListItemDefaults.colors(containerColor = if (active) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surfaceDim)
            )

            if (isExpanded) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = {
                        onThemeDelete(theme)
                        isExpanded = false
                    }, modifier = Modifier.scale(0.8f)) {
                        Text(text = stringResource(id = R.string.delete))
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                    Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(
                            text = stringResource(id = if (theme.active) R.string.active else R.string.inactive),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Switch(checked = active, onCheckedChange = {
                            onSetActive(it)
                            active = it
                        }, modifier = Modifier.scale(0.8f))
                    }
                }

            }
        }
    }
}




