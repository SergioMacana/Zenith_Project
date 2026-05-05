package com.example.zenith

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.zenith.receivers.FitnessReminderScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        ReminderScheduler.scheduleDailyMoodReminder(context)
        FitnessReminderScheduler.scheduleMorningReminder(context)
        FitnessReminderScheduler.scheduleHabitReminder(context)
        CleanupScheduler.scheduleDailyCleanup(context)
    }
}