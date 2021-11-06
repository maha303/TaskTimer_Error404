package com.example.tasktimer_error404

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer_error404.adapters.TaskAdapter
import com.example.tasktimer_error404.database.Goal
import com.example.tasktimer_error404.databinding.ActivityGoalDetailsPageBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class GoalDetailsPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details_page)
        initializeBinding()
        initializeRecycler()
        getGoalDetails()
        initializeViewModel()
        binding.taskBack.setOnClickListener {
            finish()
        }
    }

    private lateinit var taskViewModel: MainViewModel
    private fun initializeViewModel(){
        taskViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        taskViewModel.getTasks(selectedGoal.g_id).observe(this, { tasks -> adapterTask.update(tasks) })
    }

    private lateinit var binding: ActivityGoalDetailsPageBinding
    private fun initializeBinding() {
        Log.d("MAIN", "Binding Initialized!")
        binding = ActivityGoalDetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    val callback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, RIGHT or LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            when{
                LEFT == direction -> taskViewModel.deleteTask(adapterTask.getTaskID(viewHolder.adapterPosition))
                RIGHT == direction -> 1//todo add edit function
            }
            //todo add swipe ui
            //todo add undo on delete
        }
        override fun onChildDraw (c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean){
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightBackgroundColor(Color.GREEN).addSwipeLeftBackgroundColor(Color.RED) //todo change colors to prettier colors
                .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24).addSwipeLeftActionIcon(R.drawable.delete_icon)
                .setActionIconTint(Color.WHITE)
                .create().decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
    private lateinit var adapterTask: TaskAdapter
    private fun initializeRecycler() {
        adapterTask = TaskAdapter(this)
        binding.rvGoalDetailsTasks.adapter = adapterTask
        binding.rvGoalDetailsTasks.layoutManager = LinearLayoutManager(this)
        Log.d("TAG MAIN", "RV INITIALIZED")
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvGoalDetailsTasks)
    }

    private lateinit var selectedGoal: Goal
    private fun getGoalDetails() {
        var id = intent.extras?.getInt("id")!!
        var title = intent.extras?.getString("title")!!
        var description = intent.extras?.getString("description")!!
        var icon = intent.extras?.getString("icon")!!
        var state = intent.extras?.getString("state")!!
        var time = intent.extras?.getString("time")!!
        selectedGoal = Goal(id, title, description, icon, state, time)
        binding.tvGoalDetailsTitle.text = selectedGoal.g_title
        binding.tvGoalDetailsDescription.text = selectedGoal.g_description
    }

    fun addNewTask(view: View) {
        Log.d("TAG MAIN", "ADD TASK BUTTON PRESSED")
        val task = binding.etGoalDetailsAddTask.text.toString()
        binding.etGoalDetailsAddTask.text.clear()
        binding.etGoalDetailsAddTask.clearFocus()
        taskViewModel.addTask(0, task, "", "", selectedGoal.g_id)
    }

}

//todo app name
//todo add task prompt
//todo change state and time
//todo add update
//todo add swipe decorator

//todo description ui
//todo task item ui -> timer icon

/*
Tapping another task while a timer is active, should pause the active timer and start the timer of the tapped task
A new fragment/activity should display all tasks and the amount of time spent on each task
Make sure to keep track of the total time spent on each task and a total for all tasks
Spend some time making sure that users understand how to use your app (include instructions)

 */