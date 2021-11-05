package com.example.tasktimer_error404.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasktimer_error404.R
import com.example.tasktimer_error404.databinding.ActivityTimerPageBinding
import kotlin.math.roundToInt


class TimerPage : AppCompatActivity() {
    private lateinit var binding: ActivityTimerPageBinding
    private var timerStarted=false
    private lateinit var serviceIntent:Intent
    private var time=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.StartStopButton.setOnClickListener { startStopTimer() }
        binding.resetButton.setOnClickListener { resetTimer() }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIME_UPDATED))

    }
    private fun resetTimer() {
        stopTimer()
        time=0.0
        binding.timeTv.text=getTimeStringFromDouble(time)

    }

    private fun startStopTimer() {
        if(timerStarted)
            stopTimer()
             else
                startTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA,time)
        startService(serviceIntent)
//        binding.StartStopButton.text="Stop"
//        binding.StartStopButton.icon=getDrawable(R.drawable.pause)
        binding.StartStopButton.setImageResource(R.drawable.pause)

        timerStarted=true
    }

    private fun stopTimer() {

        stopService(serviceIntent)
//        binding.StartStopButton.text="Start"
//        binding.StartStopButton.icon=getDrawable(R.drawable.play_icon)
        binding.StartStopButton.setImageResource(R.drawable.play)
        timerStarted=false
    }

    private val updateTime:BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
           time=intent.getDoubleExtra(TimerService.TIME_EXTRA,0.0)
            binding.timeTv.text=getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String
    {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return makeTimeString(hours,minutes,seconds)

    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d",hour,min,sec)


}