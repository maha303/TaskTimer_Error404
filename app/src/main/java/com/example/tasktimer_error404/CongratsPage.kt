package com.example.tasktimer_error404

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.tasktimer_error404.databinding.ActivityCongratPageBinding
import com.example.tasktimer_error404.databinding.ActivityGoalPageBinding

class CongratsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congrat_page)
        initializeBinding()
        binding.tvCongrats.text = "CONGRATULATION!\n" +
                "GOAL ACHIEVED!"
    }

    private lateinit var binding: ActivityCongratPageBinding
    private fun initializeBinding() {
        Log.d("MAIN", "Binding Initialized!")
        binding = ActivityCongratPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun backButton(view: View) {
        val intent = Intent(this, GoalPage::class.java)
        startActivity(intent)    }
}