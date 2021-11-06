package com.example.tasktimer_error404.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
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
    private var task_time: Double? = 0.0
    private var goal_id: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.timeTv.text = "00:00:00"

        initializeViewModel()
        getTaskDetails()

        binding.StartStopButton.setOnClickListener { startStopTimer() }
        binding.resetButton.setOnClickListener { resetTimer() }
        binding.completedButton.setOnClickListener {
            task_state = "true"
            updateTimer()
            finish()
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
        task_time = intent.extras?.getDouble("task_time")!!
        goal_id = intent.extras?.getInt("goal_id")!!

        binding.tvTimerTitle.text = task_title
        time = task_time as Double
        binding.timeTv.text = getTimeStringFromDouble(time!!)
        //binding.timeTv.text = task_time.toString() //todo convert to string
    }

    private lateinit var taskViewModel: MainViewModel
    private fun initializeViewModel(){
        taskViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun updateTimer(){
        //todo convert 4th column
        //binding.timeTv.text.toString().toDouble()
        taskViewModel.editTask(task_id!!, task_title!!, task_state!!, task_time!!, goal_id!!)
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.timeTv.text = getTimeStringFromDouble(time)
        //todo task_time = getTimeStringFromDouble(time)
    }

    private fun startStopTimer() {
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        Log.d("MyTime", "onstart"+time.toString())
        //time = task_time

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
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context, intent: Intent) {
            if(task_time == 0.0 || task_time == null){
                time = intent.getDoubleExtra(TimerService.TIME_EXTRA,0.0)
                binding.timeTv.text = getTimeStringFromDouble(time)
            } else {
                //todo convert time
                binding.timeTv.text = getTimeStringFromDouble(time)

                //time = intent.getDoubleExtra(TimerService.TIME_EXTRA,0.0)
                //val df = SimpleDateFormat("HH:mm:ss")
                //var current_time = df.parse(task_time).time.toDouble()

                //time = intent.getDoubleExtra(TimerService.TIME_EXTRA,current_time)
                //binding.timeTv.text = getTimeStringFromDouble(time)
                //Log.d("MyTime", "current_time " + current_time.toString()+" time"+time.toString())
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