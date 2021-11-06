package com.example.tasktimer_error404.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TimerDao {

    //get all goals
    @Query("SELECT * FROM GoalsTable ORDER BY g_id ASC")
    fun getGoals(): LiveData<List<Goal>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Transaction
    @Query("SELECT * FROM TasksTable WHERE goal_id=:goal ORDER BY t_id ASC")
    fun getTasks(goal: Int): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

}
