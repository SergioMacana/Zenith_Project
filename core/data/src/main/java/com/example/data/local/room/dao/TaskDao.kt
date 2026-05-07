package com.example.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.room.entity.TaskItemEntity

@Dao
interface TaskDao {

    @Query("""
    SELECT * FROM task_items
    WHERE isCompleted = 0
    ORDER BY dueDate ASC
""")
    suspend fun getAllPendingTasks(): List<TaskItemEntity>

    @Query("""
        SELECT * FROM task_items
        WHERE isCompleted = 0
        AND dueDate BETWEEN :startOfDay AND :endOfDay
        ORDER BY dueDate ASC
    """)
    suspend fun getTasksForDay(startOfDay: Long, endOfDay: Long): List<TaskItemEntity>

    @Query("""
        SELECT * FROM task_items
        WHERE isCompleted = 0
        AND dueDate BETWEEN :startDate AND :endDate
        ORDER BY dueDate ASC
    """)
    suspend fun getTasksInRange(startDate: Long, endDate: Long): List<TaskItemEntity>

    @Query("""
        SELECT * FROM task_items
        WHERE isCompleted = 0
        AND dueDate IS NOT NULL
        AND dueDate >= :currentTime
        ORDER BY dueDate ASC
        LIMIT :limit
    """)
    suspend fun getUpcomingTasks(currentTime: Long, limit: Int): List<TaskItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskItemEntity)

    @Update
    suspend fun updateTask(task: TaskItemEntity)

    @Query("DELETE FROM task_items WHERE id = :taskId")
    suspend fun deleteTask(taskId: String)

    @Query("UPDATE task_items SET isCompleted = 1 WHERE id = :taskId")
    suspend fun completeTask(taskId: String)

    @Query("SELECT * FROM task_items WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: String): TaskItemEntity?
}