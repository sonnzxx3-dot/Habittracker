package com.example.habittracker.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.notification.HabitReminder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = HabitDatabase.get(app).habitDao()

    val habits: StateFlow<List<Habit>> = dao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addHabit(habit: Habit) = viewModelScope.launch {
        val id = dao.insert(habit)
        if (habit.reminderMinutes != null) {
            HabitReminder.schedule(getApplication(), habit.copy(id = id))
        }
    }

    fun toggleToday(habit: Habit) = viewModelScope.launch {
        val today = LocalDate.now().toString()
        val dates = habit.completedDates.toMutableList()
        if (dates.contains(today)) dates.remove(today) else dates.add(today)
        dao.update(habit.copy(completedDates = dates))
    }

    fun deleteHabit(habit: Habit) = viewModelScope.launch {
        HabitReminder.cancel(getApplication(), habit)
        dao.delete(habit)
    }

    /** Доля выполненных привычек за сегодня (0f..1f). */
    fun progressToday(list: List<Habit>): Float {
        if (list.isEmpty()) return 0f
        val today = LocalDate.now()
        val done = list.count { it.isDoneOn(today) }
        return done.toFloat() / list.size
    }
}
