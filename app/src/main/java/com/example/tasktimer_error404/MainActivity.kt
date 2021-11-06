package com.example.tasktimer_error404

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startMainActivity()
    }
    private fun startMainActivity() {
        mRunnable = Runnable {
            startActivity(Intent(this,GoalPage::class.java))
            finish()
        }

        mHandler = Handler()

        mHandler.postDelayed(mRunnable, 6000)
    }
}