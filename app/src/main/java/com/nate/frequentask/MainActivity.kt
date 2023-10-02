package com.nate.frequentask

import ThemeDetailScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.nate.frequentask.data.Theme
import com.nate.frequentask.data.ThemeRepository
import com.nate.frequentask.task.TaskDetailScreen
import com.nate.frequentask.themelist.ThemeListScreen
import com.nate.frequentask.ui.theme.FrequentAskTheme
import java.util.Date

class MainActivity() : ComponentActivity() {
    private val themeRepository by lazy { ThemeRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeList by themeRepository.themesList.observeAsState()
            FrequentAskTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    NavHost(navController = navController, startDestination = "themeList") {
                        composable("themeList") {
                            ThemeListScreen(
                                navController,
                                themeRepository = themeRepository
                            )
                        }
                        composable(
                            route = "themeDetail/{themeId}",
                            arguments = listOf(navArgument("themeId") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val themeId = backStackEntry.arguments?.getString("themeId")
                            val theme = themeList?.find { it.id == themeId }
                            if (theme != null) {
                                ThemeDetailScreen(
                                    navController = navController,
                                    themeID = themeId!!,
                                    themeRepository = themeRepository
                                )
                            }
                        }
                        composable(
                            route = "taskDetail/{themeId}/{taskId}",
                            arguments = listOf(
                                navArgument("themeId") { type = NavType.StringType },
                                navArgument("taskId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val themeId = backStackEntry.arguments?.getString("themeId")
                            val taskId = backStackEntry.arguments?.getString("taskId")
                            themeList?.find { it.id == themeId }?.also { theme ->
                                theme.tasks.find { it.id == taskId }?.also { task ->
                                    TaskDetailScreen(
                                        navController = navController,
                                        theme = theme,
                                        task = task,
                                        themeRepository = themeRepository
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



