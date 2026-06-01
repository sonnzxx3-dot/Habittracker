package com.example.habittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDate

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val iconKey: String,
    val colorValue: Long,
    val timesPerWeek: Int = 7,
    /** Даты выполнения в ISO-формате "yyyy-MM-dd", через запятую. */
    val completedDates: List<String> = emptyList(),
    /** Минуты от начала суток для напоминания (null = выключено). */
    val reminderMinutes: Int? = null
) {
    fun isDoneOn(date: LocalDate): Boolean =
        completedDates.contains(date.toString())

    /** Текущий стрик: сколько дней подряд выполнено (считая со вчера/сегодня). */
    val currentStreak: Int
        get() {
            var streak = 0
            var cursor = LocalDate.now()
            if (!isDoneOn(cursor)) cursor = cursor.minusDays(1)
            while (isDoneOn(cursor)) {
                streak++
                cursor = cursor.minusDays(1)
            }
            return streak
        }

    /** Последние 7 дней (от 6 дней назад до сегодня). */
    val lastSevenDays: List<Boolean>
        get() {
            val today = LocalDate.now()
            return (6 downTo 0).map { isDoneOn(today.minusDays(it.toLong())) }
        }
}

/** Room не хранит списки нативно — конвертируем в строку и обратно. */
class Converters {
    @TypeConverter
    fun fromList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toList(value: String): List<String> =
        if (value.isBlank()) emptyList() else value.split(",")
}
