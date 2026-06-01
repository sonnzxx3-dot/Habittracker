package com.example.habittracker.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.data.Habit
import com.example.habittracker.data.HabitColors
import com.example.habittracker.data.HabitIcons

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddHabitScreen(
    onSave: (Habit) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var iconIndex by remember { mutableIntStateOf(0) }
    var colorIndex by remember { mutableIntStateOf(0) }
    var timesPerWeek by remember { mutableIntStateOf(7) }
    var reminderMinutes by remember { mutableStateOf<Int?>(null) }

    val iconKeys = HabitIcons.keys
    val accent = HabitColors.palette[colorIndex]

    fun pickTime() {
        val initial = reminderMinutes ?: 9 * 60
        TimePickerDialog(
            context,
            { _, h, m -> reminderMinutes = h * 60 + m },
            initial / 60, initial % 60, true
        ).show()
    }

    fun timeLabel(): String {
        val m = reminderMinutes ?: return "Выключено"
        return "Каждый день в %02d:%02d".format(m / 60, m % 60)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новая привычка") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(24.dp)
        ) {
            // Предпросмотр.
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                    .background(accent.copy(alpha = 0.13f)).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(44.dp).clip(CircleShape).background(accent.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(HabitIcons.icon(iconKeys[iconIndex]), null, tint = accent)
                }
                Spacer(Modifier.size(16.dp))
                Text(
                    title.ifBlank { "Моя привычка" },
                    fontSize = 16.sp, fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название") },
                placeholder = { Text("Например, пить воду") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            Label("Иконка")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                iconKeys.forEachIndexed { i, key ->
                    val selected = i == iconIndex
                    Box(
                        Modifier.padding(vertical = 5.dp).size(48.dp).clip(CircleShape)
                            .background(
                                if (selected) accent
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { iconIndex = i },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            HabitIcons.icon(key), null,
                            tint = if (selected) Color.White
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))

            Label("Цвет")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                HabitColors.palette.forEachIndexed { i, c ->
                    val selected = i == colorIndex
                    Box(
                        Modifier.size(42.dp).clip(CircleShape).background(c)
                            .then(
                                if (selected) Modifier.border(
                                    3.dp, MaterialTheme.colorScheme.onSurface, CircleShape
                                ) else Modifier
                            )
                            .clickable { colorIndex = i },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selected) Icon(
                            Icons.Filled.Check, null,
                            tint = Color.White, modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))

            Label("Цель: $timesPerWeek раз в неделю")
            Slider(
                value = timesPerWeek.toFloat(),
                onValueChange = { timesPerWeek = it.toInt() },
                valueRange = 1f..7f,
                steps = 5
            )
            Spacer(Modifier.height(12.dp))

            // Напоминание.
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .clickable { pickTime() }.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Notifications, null, tint = accent)
                Spacer(Modifier.size(16.dp))
                Column(Modifier.weight(1f)) {
                    Text("Напоминание", fontWeight = FontWeight.Medium)
                    Text(
                        timeLabel(), fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (reminderMinutes != null) {
                    IconButton(onClick = { reminderMinutes = null }) {
                        Icon(Icons.Filled.Close, "Выключить")
                    }
                }
            }
            Spacer(Modifier.height(28.dp))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            Habit(
                                title = title.trim(),
                                iconKey = iconKeys[iconIndex],
                                colorValue = accent.toArgb().toLong() and 0xFFFFFFFFL,
                                timesPerWeek = timesPerWeek,
                                reminderMinutes = reminderMinutes
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accent)
            ) {
                Text("Сохранить", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text, fontSize = 14.sp, fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}
