package com.example.habittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.data.Habit
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(habits: List<Habit>, onBack: () -> Unit) {
    val weekData = (6 downTo 0).map { offset ->
        val day = LocalDate.now().minusDays(offset.toLong())
        if (habits.isEmpty()) 0f
        else habits.count { it.isDoneOn(day) }.toFloat() / habits.size
    }
    val bestStreak = habits.maxOfOrNull { it.currentStreak } ?: 0
    val weekCompletions = habits.sumOf { h ->
        h.lastSevenDays.count { it }
    }
    val weekAvg = if (weekData.isEmpty()) 0
    else (weekData.average() * 100).toInt()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Статистика") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                MetricCard(Modifier.weight(1f), Icons.AutoMirrored.Filled.ListAlt,
                    Color(0xFF1D9E75), "${habits.size}", "привычек")
                MetricCard(Modifier.weight(1f), Icons.Filled.LocalFireDepartment,
                    Color(0xFFD85A30), "$bestStreak дн.", "лучший стрик")
            }
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                MetricCard(Modifier.weight(1f), Icons.Filled.CheckCircle,
                    Color(0xFF378ADD), "$weekCompletions", "отметок за неделю")
                MetricCard(Modifier.weight(1f), Icons.AutoMirrored.Filled.TrendingUp,
                    Color(0xFF7F77DD), "$weekAvg%", "среднее за неделю")
            }
            Spacer(Modifier.height(32.dp))
            Text("Последние 7 дней", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(20.dp))
            WeekChart(weekData)
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier,
    icon: ImageVector,
    color: Color,
    value: String,
    label: String,
) {
    Column(
        modifier
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(18.dp)
    ) {
        Box(
            Modifier.size(34.dp).clip(RoundedCornerShape(10.dp))
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun WeekChart(data: List<Float>) {
    val labels = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
    val today = LocalDate.now()
    val barColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEachIndexed { i, ratio ->
            val dayIdx = today.minusDays((6 - i).toLong()).dayOfWeek.value - 1
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier.fillMaxWidth().height(150.dp)
                        .clip(RoundedCornerShape(6.dp)).background(trackColor),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        Modifier.fillMaxWidth()
                            .height((ratio.coerceIn(0f, 1f) * 150).dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(barColor)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(labels[dayIdx], fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
