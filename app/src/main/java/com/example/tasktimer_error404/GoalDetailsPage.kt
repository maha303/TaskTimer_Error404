package com.example.tasktimer_error404

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer_error404.adapters.TaskAdapter
import com.example.tasktimer_error404.database.Goal
import com.example.tasktimer_error404.database.Task
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
    }

    private lateinit var taskViewModel: MainViewModel
    private fun initializeViewModel(){
        taskViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        taskViewModel.getTasks(selectedGoal.g_id).observe(this, { tasks ->
            if(tasks.isEmpty()) {
                Log.d("TAG GDP", "task list is empty")
                binding.llnoTask.isVisible = true
            }
            else{
                Log.d("TAG GDP", "task list is: $tasks")
                binding.llnoTask.isVisible = false
            }
            setOverAllTime(tasks)
            adapterTask.update(tasks)
        })
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
                RIGHT == direction -> alertDialog(adapterTask.getTask(viewHolder.adapterPosition))
            }
        }
        override fun onChildDraw (c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean){
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightBackgroundColor(Color.GREEN).addSwipeLeftBackgroundColor(Color.RED) //todo change colors to prettier colors
                .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24).addSwipeLeftActionIcon(R.drawable.delete_icon)
                .setActionIconTint(Color.WHITE)
                .addSwipeLeftLabel("DELETE").setSwipeLeftLabelColor(Color.WHITE).setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
                .addSwipeRightLabel("EDIT").setSwipeRightLabelColor(Color.WHITE).setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
                .create().decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
    private lateinit var adapterTask: TaskAdapter
    private fun initializeRecycler() {
        adapterTask = TaskAdapter(this, this)
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
        var state = intent.extras?.getString("state")!!
        var time = intent.extras?.getDouble("time")!!
        selectedGoal = Goal(id, title, description, state, time)
        binding.tvGoalDetailsTitle.text = selectedGoal.g_title
        binding.tvGoalDetailsDescription.text = selectedGoal.g_description
    }

    fun addNewTask(view: View) {
        Log.d("TAG MAIN", "ADD TASK BUTTON PRESSED")
        val task = binding.etGoalDetailsAddTask.text.toString()
        binding.etGoalDetailsAddTask.text.clear()
        binding.etGoalDetailsAddTask.clearFocus()
        taskViewModel.addTask(0, task, "false", 0.0, selectedGoal.g_id)
    }

    fun setState(task: Task, state: Boolean){
        taskViewModel.editTask(task.t_id, task.t_title, state.toString(), task.t_time, task.goal_id)
    }

    fun backButton(view: View) {
        val intent = Intent(this, GoalPage::class.java)
        startActivity(intent)
    }

    fun alertDialog(task: Task) {
        val dialogBuilder = AlertDialog.Builder(this)
        val newLayout = LinearLayout(this)
        newLayout.orientation = LinearLayout.VERTICAL
            val newTask = EditText(this)
            newTask.hint = task.t_title
            newLayout.addView(newTask)
            Log.d("TAG ALERT", "INSIDE UPDATE")
            dialogBuilder
                .setPositiveButton("OK") { _, _ ->
                    Log.d("TAG ALERT", "INSIDE POS BUTTON")
                    Log.d("TAG ALERT", "task IS: $task")
                    taskViewModel.editTask(task.t_id, newTask.text.toString(), task.t_state, task.t_time, task.goal_id)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                    taskViewModel.editTask(task.t_id, task.t_title, task.t_state, task.t_time, task.goal_id)
                }

        val alert = dialogBuilder.create()
        alert.setTitle("Edit Task")
        alert.setView(newLayout)
        alert.show()
    }

    fun completeGoal(view: View) {
        //todo make a prompt dialog: are you sure you are done?
        val goal = selectedGoal
        taskViewModel.editGoal(goal.g_id, goal.g_title, goal.g_description, true.toString(), goal.g_time)

    }

    private fun setOverAllTime(tasks: List<Task>){
        var sum = 0.0
        for (i in tasks.indices){
            sum += tasks[i].t_time
        }
        val goal = selectedGoal
        taskViewModel.editGoal(goal.g_id, goal.g_title, goal.g_description, goal.g_state, sum)
    }
}

//todo app name

//Lubabah
//todo instructions

//Lina
//todo alert dialog pretty ui (optional)
//todo 5 alert dialogs (2 in goal details when swipe + 2 in goal when swipe + 1 when goal complete)
//todo display task time for each task - similar to goal
