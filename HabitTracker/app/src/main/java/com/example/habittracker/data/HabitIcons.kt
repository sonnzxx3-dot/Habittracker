package com.example.habittracker.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SmokeFree
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/** Карта ключ → иконка. В базе храним только строковый ключ. */
object HabitIcons {
    val all: Map<String, ImageVector> = linkedMapOf(
        "water" to Icons.Filled.WaterDrop,
        "fitness" to Icons.Filled.FitnessCenter,
        "book" to Icons.Filled.Book,
        "meditation" to Icons.Filled.SelfImprovement,
        "run" to Icons.Filled.DirectionsRun,
        "sleep" to Icons.Filled.Bedtime,
        "plant" to Icons.Filled.LocalFlorist,
        "code" to Icons.Filled.Code,
        "art" to Icons.Filled.Brush,
        "music" to Icons.Filled.MusicNote,
        "nosmoke" to Icons.Filled.SmokeFree,
        "food" to Icons.Filled.Restaurant,
    )

    fun icon(key: String): ImageVector = all[key] ?: Icons.Filled.WaterDrop
    val keys: List<String> get() = all.keys.toList()
}

object HabitColors {
    val palette = listOf(
        Color(0xFF1D9E75),
        Color(0xFF378ADD),
        Color(0xFF7F77DD),
        Color(0xFFD85A30),
        Color(0xFFD4537E),
        Color(0xFFBA7517),
    )
}
