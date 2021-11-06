package com.example.tasktimer_error404.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.tasktimer_error404.*
import com.example.tasktimer_error404.database.Goal
import com.example.tasktimer_error404.database.Task
import com.example.tasktimer_error404.databinding.ActivityTimerPageBinding
import kotlin.math.roundToInt


class TimerPage : AppCompatActivity() {
    private lateinit var binding: ActivityTimerPageBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent

    private var time = 0.0
    private var task_id: Int? = 0
    private var task_title: String? = ""
    private var task_state: String? = ""
    private var task_time: String? = ""
    private var goal_id: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViewModel()
        getTaskDetails()

        binding.StartStopButton.setOnClickListener { startStopTimer() }
        binding.resetButton.setOnClickListener { resetTimer() }
        binding.completedButton.setOnClickListener {
            task_state = "true"
            updateTimer()
            val intent = Intent(this, CongratPage::class.java)
            startActivity(intent)
        }
        binding.timerHome.setOnClickListener {
            updateTimer()
            val intent = Intent(this, GoalPage::class.java)
            startActivity(intent)
        }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIME_UPDATED))

    }

    private fun getTaskDetails() {
        task_id = intent.extras?.getInt("task_id")!!
        task_title = intent.extras?.getString("task_title")!!
        task_state = intent.extras?.getString("task_state")!!
        task_time = intent.extras?.getString("task_time")!!
        goal_id = intent.extras?.getInt("goal_id")!!

        binding.tvTimerTitle.text = task_title
        binding.timeTv.text = task_time
    }

    private lateinit var taskViewModel: MainViewModel
    private fun initializeViewModel(){
        taskViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun updateTimer(){
        taskViewModel.editTask(task_id!!, task_title!!, task_state!!, binding.timeTv.text.toString(), goal_id!!)
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.timeTv.text = getTimeStringFromDouble(time)
        task_time = getTimeStringFromDouble(time)
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
        binding.StartStopButton.setImageResource(R.drawable.pause)

        timerStarted=true
    }

    private fun stopTimer() {

        stopService(serviceIntent)
        binding.StartStopButton.setImageResource(R.drawable.play)
        timerStarted=false
        updateTimer()
    }

    private val updateTime:BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            if(task_time == "00:00:00" || task_time == null || task_time == ""){
                time = intent.getDoubleExtra(TimerService.TIME_EXTRA,0.0)
                binding.timeTv.text = getTimeStringFromDouble(time)
            } else {
                binding.timeTv.text = task_time
            }
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