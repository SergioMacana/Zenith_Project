package com.example.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.room.dao.TaskDao
import com.example.data.local.room.entity.TaskItemEntity

@Database(
    entities = [TaskItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ZenithDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: ZenithDatabase? = null

        fun getInstance(context: Context): ZenithDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ZenithDatabase::class.java,
                    "zenith_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}