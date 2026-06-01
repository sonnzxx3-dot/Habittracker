package com.example.habittracker

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.data.HabitViewModel
import com.example.habittracker.data.SettingsRepository
import com.example.habittracker.data.ThemeMode
import com.example.habittracker.ui.screens.AddHabitScreen
import com.example.habittracker.ui.screens.SettingsScreen
import com.example.habittracker.ui.screens.StatsScreen
import com.example.habittracker.ui.screens.TodayScreen
import com.example.habittracker.ui.theme.HabitTrackerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val settings = SettingsRepository(applicationContext)

        setContent {
            val vm: HabitViewModel = viewModel()
            val habits by vm.habits.collectAsState()
            val theme by settings.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val scope = rememberCoroutineScope()

            HabitTrackerTheme(themeMode = theme) {
                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = "today") {
                    composable("today") {
                        TodayScreen(
                            habits = habits,
                            progress = vm.progressToday(habits),
                            onToggle = vm::toggleToday,
                            onDelete = vm::deleteHabit,
                            onAdd = { nav.navigate("add") },
                            onStats = { nav.navigate("stats") },
                            onSettings = { nav.navigate("settings") },
                        )
                    }
                    composable("add") {
                        AddHabitScreen(
                            onSave = {
                                vm.addHabit(it)
                                nav.popBackStack()
                            },
                            onBack = { nav.popBackStack() }
                        )
                    }
                    composable("stats") {
                        StatsScreen(habits = habits, onBack = { nav.popBackStack() })
                    }
                    composable("settings") {
                        SettingsScreen(
                            currentTheme = theme,
                            onThemeChange = { scope.launch { settings.setTheme(it) } },
                            onBack = { nav.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
