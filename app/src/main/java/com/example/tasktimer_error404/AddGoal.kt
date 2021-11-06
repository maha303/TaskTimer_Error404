package com.example.tasktimer_error404

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasktimer_error404.adapters.GoalAdapter
import com.example.tasktimer_error404.databinding.ActivityGoalPageBinding

class AddGoal : AppCompatActivity() {

    private lateinit var saveButton: Button
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText

    private val STATUS: String = "false"
    private val TIME: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        initializeViewModel()
        etTitle =findViewById(R.id.etTitle)
        etDescription =findViewById(R.id.etDescription)

        saveButton = findViewById(R.id.sanebtn)
        saveButton.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()

            initializeViewModel()
            goalViewModel.addGoal(title,description,STATUS,TIME)

            etTitle.text.clear()
            etTitle.clearFocus()

            etDescription.text.clear()
            etDescription.clearFocus()

            val intent = Intent(this, GoalPage::class.java)
            startActivity(intent)
        }
    }
    fun backButton(view: View) {
        finish()
    }
    private lateinit var goalViewModel: MainViewModel
    private fun initializeViewModel(){
        goalViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }
}