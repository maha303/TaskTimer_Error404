package com.example.tasktimer_error404

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasktimer_error404.databinding.ActivityCongratPageBinding

class CongratPage : AppCompatActivity() {
    private lateinit var binding: ActivityCongratPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congrat_page)
        binding.congratBtn.setOnClickListener {
            finish()
           //todo go back to goal details page
        }
    }
}