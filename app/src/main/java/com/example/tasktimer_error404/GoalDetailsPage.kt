package com.example.tasktimer_error404

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tasktimer_error404.databinding.ActivityMainBinding

class GoalDetailsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details_page)
        initializeBinding()
    }

    private lateinit var binding: ActivityMainBinding
    private fun initializeBinding() {
        Log.d("MAIN", "Binding Initialized!")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}