package com.example.tasktimer_error404.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TasksTable")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val t_id: Int,
    val t_title: String,
    val t_state: String,
    val t_time: Double,
    val goal_id: Int)