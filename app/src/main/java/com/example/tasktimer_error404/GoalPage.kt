package com.example.tasktimer_error404

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer_error404.adapters.GoalAdapter
import com.example.tasktimer_error404.adapters.TaskAdapter
import com.example.tasktimer_error404.database.Goal
import com.example.tasktimer_error404.database.Task
import com.example.tasktimer_error404.databinding.ActivityGoalDetailsPageBinding
import com.example.tasktimer_error404.databinding.ActivityGoalPageBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class GoalPage : AppCompatActivity() {

    private lateinit var goal_add: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_page)

        initializeBinding()
        initializeRecycler()
        initializeViewModel()

        goal_add = findViewById(R.id.goal_add)
        goal_add.setOnClickListener{
            val intent = Intent(this, AddGoal::class.java)
            startActivity(intent)
        }
    }

    private lateinit var binding: ActivityGoalPageBinding
    private fun initializeBinding() {
        Log.d("MAIN", "Binding Initialized!")
        binding = ActivityGoalPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private lateinit var adapterGoal: GoalAdapter
    private fun initializeRecycler() {
        adapterGoal = GoalAdapter(this)
        binding.rvGoals.adapter = adapterGoal
        binding.rvGoals.layoutManager = LinearLayoutManager(this)
        Log.d("TAG MAIN", "RV INITIALIZED")
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvGoals)
    }

    val callback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            when{
                ItemTouchHelper.LEFT == direction -> deleteDialog(adapterGoal.getGoal(viewHolder.adapterPosition))
                ItemTouchHelper.RIGHT == direction -> alertDialog(adapterGoal.getGoal(viewHolder.adapterPosition))
            }

        }
        override fun onChildDraw (c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean){
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightBackgroundColor(Color.GREEN).addSwipeLeftBackgroundColor(Color.RED) //todo change colors to prettier colors
                .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24).addSwipeLeftActionIcon(R.drawable.delete_icon)
                .setActionIconTint(Color.WHITE)
                .addSwipeLeftLabel("DELETE").setSwipeLeftLabelColor(Color.WHITE).setSwipeLeftLabelTextSize(
                    TypedValue.COMPLEX_UNIT_DIP, 18f)
                .addSwipeRightLabel("EDIT").setSwipeRightLabelColor(Color.WHITE).setSwipeRightLabelTextSize(
                    TypedValue.COMPLEX_UNIT_DIP, 18f)
                .create().decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    private lateinit var goalViewModel: MainViewModel
    private fun initializeViewModel(){
        goalViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        goalViewModel.getGoals().observe(this, { goals -> adapterGoal.updateRecycleView(goals) })
    }

    fun alertDialog(goal: Goal){
        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.alartdialog, null)
        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(this).setView(messageBoxView)
        val  messageBoxInstance = messageBoxBuilder.show()
        var title = goal.g_title
        var descriptions = goal.g_description
        //setting text values
    val et_g_t_title=messageBoxInstance.findViewById<EditText>(R.id.et_g_t_title)
    val et_g_t_descriptions=messageBoxInstance.findViewById<EditText>(R.id.et_g_t_descriptions)
    val editBtn_ok=messageBoxInstance.findViewById<Button>(R.id.editBtn_ok)
    val editBtn_cancel=messageBoxInstance.findViewById<Button>(R.id.editBtn_cancel)
        et_g_t_title.hint= goal.g_title
        et_g_t_descriptions.hint= goal.g_description
        editBtn_ok.setOnClickListener {
            if(et_g_t_title.text.isNotEmpty()){
                title=et_g_t_title.text.toString()
            }
            if(et_g_t_descriptions.text.isNotEmpty()){
                descriptions=et_g_t_descriptions.text.toString()
            }
            goalViewModel.editGoal(goal.g_id, title, descriptions,  goal.g_state, goal.g_time)
            messageBoxInstance.dismiss()
        }
        //set Listener
        editBtn_cancel.setOnClickListener(){
            //close dialog
            goalViewModel.editGoal(goal.g_id, goal.g_title, goal.g_description,  goal.g_state, goal.g_time)
            messageBoxInstance.dismiss()
        }
    }
    fun deleteDialog(goal: Goal){
        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.dialog, null)
        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(this).setView(messageBoxView)
        val  messageBoxInstance = messageBoxBuilder.show()

        //setting text values
        val ok_Btn=messageBoxInstance.findViewById<Button>(R.id.ok_Btn)
        val cancel_Btn=messageBoxInstance.findViewById<Button>(R.id.cancel_Btn)

        ok_Btn.setOnClickListener {
            goalViewModel.deleteGoal(goal.g_id)
            messageBoxInstance.dismiss()
        }
        //set Listener
        cancel_Btn.setOnClickListener(){
            //close dialog
            goalViewModel.editGoal(goal.g_id, goal.g_title, goal.g_description,  goal.g_state, goal.g_time)
            messageBoxInstance.dismiss()
        }
    }
}