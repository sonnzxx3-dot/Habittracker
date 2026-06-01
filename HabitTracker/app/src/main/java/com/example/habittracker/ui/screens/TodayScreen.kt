package com.example.habittracker.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.data.Habit
import com.example.habittracker.data.HabitIcons
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TodayScreen(
    habits: List<Habit>,
    progress: Float,
    onToggle: (Habit) -> Unit,
    onDelete: (Habit) -> Unit,
    onAdd: () -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAdd,
                icon = { Icon(Icons.Filled.Add, null) },
                text = { Text("Привычка") }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Header(progress, habits.isNotEmpty() && progress >= 1f, onStats, onSettings)
            if (habits.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 16.dp, vertical = 4.dp
                    )
                ) {
                    items(habits, key = { it.id }) { habit ->
                        HabitCard(habit, onToggle, onDelete)
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun Header(
    progress: Float,
    allDone: Boolean,
    onStats: () -> Unit,
    onSettings: () -> Unit,
) {
    val today = LocalDate.now()
    val weekday = today.dayOfWeek
        .getDisplayName(TextStyle.FULL, Locale("ru"))
        .replaceFirstChar { it.uppercase() }
    val month = today.month.getDisplayName(TextStyle.FULL, Locale("ru"))
    val animated by animateFloatAsState(progress, label = "progress")

    Column(Modifier.padding(24.dp, 20.dp, 24.dp, 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Сегодня", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(
                    "$weekday, ${today.dayOfMonth} $month",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            FilledIconButton(onClick = onStats) {
                Icon(Icons.Filled.BarChart, "Статистика")
            }
            Spacer(Modifier.size(8.dp))
            FilledIconButton(onClick = onSettings) {
                Icon(Icons.Filled.Settings, "Настройки")
            }
        }
        Spacer(Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = { animated },
            modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (allDone) "🎉 Все привычки выполнены!"
            else "${(progress * 100).toInt()}% выполнено",
            fontSize = 14.sp,
            fontWeight = if (allDone) FontWeight.SemiBold else FontWeight.Normal,
            color = if (allDone) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun HabitCard(
    habit: Habit,
    onToggle: (Habit) -> Unit,
    onDelete: (Habit) -> Unit,
) {
    val done = habit.isDoneOn(LocalDate.now())
    val color = Color(habit.colorValue)
    val bg by animateColorAsState(
        if (done) color.copy(alpha = 0.13f)
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        label = "bg"
    )

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .clickable { onToggle(habit) }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(46.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(HabitIcons.icon(habit.iconKey), null, tint = color)
            }
            Spacer(Modifier.size(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    habit.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (done) TextDecoration.LineThrough else null,
                    color = if (done) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
                )
                if (habit.currentStreak > 0) {
                    Text(
                        "🔥 ${habit.currentStreak} дн. подряд",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            val scale by animateFloatAsState(if (done) 1f else 0f, label = "check")
            if (scale > 0.01f) {
                Icon(
                    Icons.Filled.CheckCircle, null, tint = color,
                    modifier = Modifier.size((30 * scale).dp)
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            habit.lastSevenDays.forEach { filled ->
                Box(
                    Modifier.size(22.dp).clip(CircleShape)
                        .background(
                            if (filled) color
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (filled) Icon(
                        Icons.Filled.Check, null,
                        tint = Color.White, modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.Spa, null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
            Spacer(Modifier.height(20.dp))
            Text("Начни с первой привычки", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(8.dp))
            Text(
                "Маленькие шаги каждый день\nприводят к большим переменам",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
