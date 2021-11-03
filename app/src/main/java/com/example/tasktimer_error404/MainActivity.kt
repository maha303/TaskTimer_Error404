package com.example.tasktimer_error404

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val l = findViewById<ConstraintLayout>(R.id.llcon)
        l.setOnClickListener {
            val intt = Intent(this,TimerPage::class.java)
            startActivity(intt)
        }
    }
}