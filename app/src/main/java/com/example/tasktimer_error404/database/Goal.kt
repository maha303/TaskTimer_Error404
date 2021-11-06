package com.example.tasktimer_error404.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "GoalsTable")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val g_id: Int,
    val g_title: String,
    val g_description: String,
    val g_state: String,
    val g_time: Double)
