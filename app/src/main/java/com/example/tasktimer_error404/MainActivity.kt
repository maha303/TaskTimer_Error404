package com.example.tasktimer_error404

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.tasktimer_error404.timer.TimerPage

class MainActivity : AppCompatActivity() {
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startMainActivity()
//        val l = findViewById<ConstraintLayout>(R.id.llcon)
//        l.setOnClickListener {
//            val intt = Intent(this,TimerPage::class.java)
//            startActivity(intt)
//        }
    }
    private fun startMainActivity() {
        mRunnable = Runnable {
            startActivity(Intent(this, TimerPage::class.java))
            finish()
        }

        mHandler = Handler()

        mHandler.postDelayed(mRunnable, 4000)
    }
}