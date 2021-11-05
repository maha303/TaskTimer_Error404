package com.example.tasktimer_error404

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tasktimer_error404.database.Goal
import com.example.tasktimer_error404.database.Task
import com.example.tasktimer_error404.database.TimerDatabase
import com.example.tasktimer_error404.database.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
        private val repository: TimerRepository
        private val goals: LiveData<List<Goal>>

        init {
            val timerDao = TimerDatabase.getDatabase(application).timerDao()
            repository = TimerRepository(timerDao)
            goals = repository.getGoals
        }

        fun getGoals(): LiveData<List<Goal>> {
            Log.d("TAG VIEW MODEL", "GET GOAL FUNCTION")
            return goals
        }

        fun addGoal(goalTitle: String, goalDescription : String, goalIcon: String, goalState: String, g_time : String){
            Log.d("TAG VIEW MODEL", "ADD GOAL FUNCTION")
            CoroutineScope(Dispatchers.IO).launch {
                repository.addGoal(Goal(0,goalTitle, goalDescription, goalIcon, goalState, g_time)) //no need to specify the id bcz it's auto
            }
        }

        fun editGoal(goalID: Int, goalTitle: String, goalDescription : String, goalIcon: String, goalState: String, g_time : String){
            CoroutineScope(Dispatchers.IO).launch {
                repository.updateGoal(Goal(goalID,goalTitle, goalDescription, goalIcon, goalState, g_time))
            }
        }

        fun deleteGoal(goalID: Int){
            CoroutineScope(Dispatchers.IO).launch {
                repository.deleteGoal(Goal(goalID,"","","","",""))
            }
        }

    fun getTasks(goalID: Int): LiveData<List<Task>> { //todo add parameter
        return repository.getTasks_fromGoal(goalID)
    }

        fun addTask(taskID: Int, taskTitle: String, taskState: String, taskTime : String, goalID: Int){
            Log.d("TAG VIEW MODEL", "ADD TASK FUNCTION")
            CoroutineScope(Dispatchers.IO).launch {
                repository.addTask(Task(0,taskTitle, taskState, taskTime, goalID)) //no need to specify the id bcz it's auto
            }
        }

        fun editTask(taskID: Int, taskTitle: String, taskState: String, taskTime : String, goalID: Int){
            CoroutineScope(Dispatchers.IO).launch {
                repository.updateTask(Task(taskID,taskTitle, taskState, taskTime, goalID))
            }
        }

        fun deleteTask(taskID: Int){
            CoroutineScope(Dispatchers.IO).launch {
                repository.deleteTask(Task(taskID,"","","",0))
            }
        }
}