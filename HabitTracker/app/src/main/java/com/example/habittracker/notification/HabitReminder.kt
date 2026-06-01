package com.example.habittracker.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.habittracker.data.Habit
import java.util.Calendar

object HabitReminder {

    private fun pendingIntent(context: Context, habit: Habit): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("title", habit.title)
            putExtra("id", habit.id.toInt())
        }
        return PendingIntent.getBroadcast(
            context, habit.id.toInt(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun schedule(context: Context, habit: Habit) {
        val minutes = habit.reminderMinutes ?: return
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, minutes / 60)
            set(Calendar.MINUTE, minutes % 60)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            target.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent(context, habit)
        )
    }

    fun cancel(context: Context, habit: Habit) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent(context, habit))
    }
}
