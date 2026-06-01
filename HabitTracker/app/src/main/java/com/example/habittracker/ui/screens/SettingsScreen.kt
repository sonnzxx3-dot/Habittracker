package com.example.habittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.ChevronRight
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.data.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            SectionTitle("Внешний вид")
            ThemeOption("Системная", Icons.Filled.BrightnessAuto,
                ThemeMode.SYSTEM, currentTheme, onThemeChange)
            ThemeOption("Светлая", Icons.Filled.LightMode,
                ThemeMode.LIGHT, currentTheme, onThemeChange)
            ThemeOption("Тёмная", Icons.Filled.Brightness4,
                ThemeMode.DARK, currentTheme, onThemeChange)

            Spacer(Modifier.size(24.dp))
            SectionTitle("О приложении")
            InfoRow(Icons.Filled.FavoriteBorder, "Убрать рекламу",
                "Поддержать разработку (скоро)", showChevron = true)
            InfoRow(Icons.Filled.Info, "Версия", "1.0.0", showChevron = false)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text, fontSize = 14.sp, fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 12.dp)
    )
}

@Composable
private fun ThemeOption(
    label: String,
    icon: ImageVector,
    value: ThemeMode,
    current: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
) {
    val selected = value == current
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
            .clickable { onSelect(value) }.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null)
        Spacer(Modifier.size(12.dp))
        Text(label, Modifier.weight(1f))
        if (selected) Icon(
            Icons.Filled.CheckCircle, null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    showChevron: Boolean,
) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null)
        Spacer(Modifier.size(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title)
            if (!showChevron) {
                Text(subtitle, fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Text(subtitle, fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (showChevron) Icon(Icons.Outlined.ChevronRight, null)
    }
}
