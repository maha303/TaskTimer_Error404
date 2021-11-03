package com.example.tasktimer_error404

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer_error404.databinding.TaskItemBinding

class Recycler(): RecyclerView.Adapter<Recycler.ViewHolder>() {
    private var messages: ArrayList<String> = ArrayList() //todo set type to entity class
    class ViewHolder(val binding: TaskItemBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.binding.apply {
            tvTaskItemTitle.text = "Task Title" //todo get message.title
            ivTaskItem.setOnClickListener {
                //todo move to timer activity + move title text
            }
            cbTaskItem.setOnClickListener{
                if (cbTaskItem.isChecked){
                    tvTaskItemTitle.paintFlags = tvTaskItemTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvTaskItemTitle.text //todo change color to grey
                    ivTaskItem.isClickable = false
                }
                else {
                    tvTaskItemTitle.paintFlags = 0
                    //todo return color to black
                    ivTaskItem.isClickable = true
                }
            }
        }
    }

    override fun getItemCount() = messages.size

    fun update(newMessages: ArrayList<String>){ //todo set type to entity class
        this.messages = newMessages
        notifyDataSetChanged()
    }
}