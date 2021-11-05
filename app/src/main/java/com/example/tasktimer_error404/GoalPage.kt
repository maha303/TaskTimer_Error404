package com.example.tasktimer_error404

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasktimer_error404.database.Goal

class GoalPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_page)


        //todo hi zahrah :)
        //todo I am using this to get the goal, edit it as you see fit
        var selectedGoal = Goal(1,"Work Out Goal","make it optional", "", "", "")
        val intent = Intent(this, GoalDetailsPage::class.java)
        intent.putExtra("id", selectedGoal.g_id)
        intent.putExtra("title", selectedGoal.g_title)
        intent.putExtra("description", selectedGoal.g_description)
        intent.putExtra("icon", selectedGoal.g_icon)
        intent.putExtra("state", selectedGoal.g_state)
        intent.putExtra("time", selectedGoal.g_time)
        startActivity(intent)

    }
}