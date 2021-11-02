package com.example.tasktimer_error404

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.tasktimer_error404.databinding.ActivityGoalDetailsPageBinding

class GoalDetailsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details_page)
        initializeBinding()
        getGoalDetails()
    }

    private lateinit var binding: ActivityGoalDetailsPageBinding
    private fun initializeBinding() {
        Log.d("MAIN", "Binding Initialized!")
        binding = ActivityGoalDetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getGoalDetails() {
        binding.tvGoalDetailsTitle.text = "GOAL" //todo later read from database
        binding.tvGoalDetailsDescription.text = "DESCRIPTION" //todo later read from database
    }

    fun addNewTask(view: View) {
        val task = binding.etGoalDetailsAddTask.text.toString()
        binding.etGoalDetailsAddTask.text.clear()
        binding.etGoalDetailsAddTask.clearFocus()
        //todo add task to database
        //todo update RV // if live data then auto
    }

}

/* todo
add RV for tasks
add logic to ViewModel
 */