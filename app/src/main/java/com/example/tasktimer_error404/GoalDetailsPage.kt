package com.example.tasktimer_error404

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
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
            current_task = tasks
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
                LEFT == direction -> delDialog(adapterTask.getTask(viewHolder.adapterPosition))
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

    lateinit var current_task: List<Task>
    fun backButton(view: View) {
        val intent = Intent(this, GoalPage::class.java)
        startActivity(intent)
    }


    fun completeGoal(view: View) {
//        val goal = selectedGoal
//        taskViewModel.editGoal(goal.g_id, goal.g_title, goal.g_description, true.toString(), goal.g_time)
        Dialog()
    }

    private fun setOverAllTime(tasks: List<Task>){
        var sum = 0.0
        for (i in tasks.indices){
            sum += tasks[i].t_time
        }
        val goal = selectedGoal
        taskViewModel.editGoal(goal.g_id, goal.g_title, goal.g_description, goal.g_state, sum)
        Log.d("Add_Over_All_Time", "Add_Over_All_Time: " + sum)
    }

    fun alertDialog(task: Task){
        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.edit_alartdialog, null)
        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(this).setView(messageBoxView)
        val  messageBoxInstance = messageBoxBuilder.show()
        var pretask = task.t_title
        //setting text values
        val et_g_t_task=messageBoxInstance.findViewById<EditText>(R.id.et_g_t_task)
        val edit_Btn_ok=messageBoxInstance.findViewById<Button>(R.id.edit_Btn_ok)
        val edit_Btn_cancel=messageBoxInstance.findViewById<Button>(R.id.edit_Btn_cancel)
        et_g_t_task.setText(task.t_title)
        edit_Btn_ok.setOnClickListener {
            if(et_g_t_task.text.isNotEmpty()){
                pretask=et_g_t_task.text.toString()
            }
            taskViewModel.editTask(task.t_id, pretask, task.t_state, task.t_time, task.goal_id)

            messageBoxInstance.dismiss()
        }
        //set Listener
        edit_Btn_cancel.setOnClickListener(){
            //close dialog
            taskViewModel.editTask(task.t_id, task.t_title, task.t_state, task.t_time, task.goal_id)
            messageBoxInstance.dismiss()
        }
    }

    fun delDialog(task:Task){
        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.dialog, null)
        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(this).setView(messageBoxView)
        val  messageBoxInstance = messageBoxBuilder.show()
        //setting text values
        val ok_Btn=messageBoxInstance.findViewById<Button>(R.id.ok_Btn)
        val cancel_Btn=messageBoxInstance.findViewById<Button>(R.id.cancel_Btn)

        ok_Btn.setOnClickListener {
            taskViewModel.deleteTask(task.t_id)
            messageBoxInstance.dismiss()
        }
        //set Listener
        cancel_Btn.setOnClickListener(){
            //close dialog
            taskViewModel.editTask(task.t_id, task.t_title, task.t_state, task.t_time, task.goal_id)
            messageBoxInstance.dismiss()
        }
    }

    fun Dialog(){
        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.dialog, null)
        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(this).setView(messageBoxView)
        val  messageBoxInstance = messageBoxBuilder.show()

        //setting text values
        val ok_Btn=messageBoxInstance.findViewById<Button>(R.id.ok_Btn)
        val cancel_Btn=messageBoxInstance.findViewById<Button>(R.id.cancel_Btn)

        ok_Btn.setOnClickListener {
            val goal = selectedGoal
            taskViewModel.editGoal(goal.g_id, goal.g_title, goal.g_description, true.toString(), goal.g_time)
            messageBoxInstance.dismiss()
            val intent = Intent(this, CongratsPage::class.java)
            startActivity(intent)
        }
        //set Listener
        cancel_Btn.setOnClickListener(){
            //close dialog
            messageBoxInstance.dismiss()
        }
    }
}

//todo app name

//Lubabah
//todo instructions

//Lina
//todo display task time for each task - similar to goal
