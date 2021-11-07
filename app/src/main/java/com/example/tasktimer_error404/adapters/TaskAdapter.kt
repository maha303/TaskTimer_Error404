package com.example.tasktimer_error404.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer_error404.GoalDetailsPage
import com.example.tasktimer_error404.GoalPage
import com.example.tasktimer_error404.MainActivity
import com.example.tasktimer_error404.database.Goal
import com.example.tasktimer_error404.database.Task
import com.example.tasktimer_error404.databinding.TaskItemBinding
import com.example.tasktimer_error404.timer.TimerPage

class TaskAdapter (val context: Context, val activity: GoalDetailsPage): RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    var messages = emptyList<Task>()
    var selecteTaskID: Int? = null

    class ViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.binding.apply {
            tvTaskItemTitle.text = message.t_title
            if(message.t_state == true.toString()) {
                cbTaskItem.isChecked = true
                tvTaskItemTitle.paintFlags = tvTaskItemTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvTaskItemTitle.setTextColor(Color.GRAY)
                ivTaskItem.isClickable = false
            }
            else {
                cbTaskItem.isChecked = false
                tvTaskItemTitle.paintFlags = 0
                tvTaskItemTitle.setTextColor(Color.BLACK)
                ivTaskItem.isClickable = true
            }
            ivTaskItem.setOnClickListener {
                val intent = Intent(context, TimerPage::class.java)
                intent.putExtra("task_id", message.t_id)
                intent.putExtra("task_title", message.t_title)
                intent.putExtra("task_state", message.t_state)
                intent.putExtra("task_time", message.t_time)
                intent.putExtra("goal_id", message.goal_id)

                context.startActivity(intent)
            }
            cbTaskItem.setOnClickListener {
                if (cbTaskItem.isChecked) { // press cb --> completed
                    activity.setState(message, true)
                } else { //incomplete
                    activity.setState(message, false)
                }
            }
            cvTaskItem.setOnClickListener { selecteTaskID = message.t_id }

        }
    }

    override fun getItemCount() = messages.size

    fun update(newMessages: List<Task>) {
        this.messages = newMessages
        notifyDataSetChanged()
    }

    fun getTaskID(position: Int): Int {
        var message = messages[position]
        return message.t_id
    }

    fun getTask(position: Int): Task{
        var message = messages[position]
        return message
    }
}