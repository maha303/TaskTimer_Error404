package com.example.tasktimer_error404.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer_error404.database.Task
import com.example.tasktimer_error404.databinding.TaskItemBinding

class TaskAdapter ():
    RecyclerView.Adapter<TaskAdapter.ItemViewHolder>() {
    private var tasks = emptyList<Task>()

    class ItemViewHolder(val binding: TaskItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ItemViewHolder {
        return ItemViewHolder(
            TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskAdapter.ItemViewHolder, position: Int) {
        val task = tasks[position]

        holder.binding.apply {
//            taskTextView.text = task.t_title +" "+ task.t_state +" "+ task.t_time +" "+ task.goal_id //name of the entity column


        }
    }

    override fun getItemCount() = tasks.size

    fun updateRecycleView(new_tasks: List<Task>) {
        this.tasks = new_tasks
        notifyDataSetChanged()
    }
}