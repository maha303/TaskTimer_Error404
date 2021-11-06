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
import android.widget.EditText
import android.widget.LinearLayout
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
                ItemTouchHelper.LEFT == direction -> goalViewModel.deleteGoal(adapterGoal.getGoalID(viewHolder.adapterPosition))
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

    fun alertDialog(goal: Goal) {
        val dialogBuilder = AlertDialog.Builder(this)
        val newLayout = LinearLayout(this)
        newLayout.orientation = LinearLayout.VERTICAL

        val titleGoal = EditText(this)
        titleGoal.setText(goal.g_title)

        val descriptionGoal = EditText(this)
        descriptionGoal.setText(goal.g_description)

        newLayout.addView(titleGoal)
        newLayout.addView(descriptionGoal)

        Log.d("TAG ALERT", "INSIDE UPDATE")
        dialogBuilder
            .setPositiveButton("OK") { _, _ ->
                Log.d("TAG ALERT", "INSIDE POS BUTTON")
                Log.d("TAG ALERT", "goal IS: $goal")
                goalViewModel.editGoal(goal.g_id, titleGoal.text.toString(), descriptionGoal.text.toString(),  goal.g_state, goal.g_time)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
                goalViewModel.editGoal(goal.g_id, goal.g_title, goal.g_description,  goal.g_state, goal.g_time)
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Edit Goal")
        alert.setView(newLayout)
        alert.show()
    }
}