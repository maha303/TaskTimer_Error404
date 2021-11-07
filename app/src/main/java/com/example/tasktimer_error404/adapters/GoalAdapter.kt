package com.example.tasktimer_error404.adapters
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer_error404.GoalDetailsPage
import com.example.tasktimer_error404.GoalPage
import com.example.tasktimer_error404.MainActivity
import com.example.tasktimer_error404.database.Goal
import com.example.tasktimer_error404.database.Task
import com.example.tasktimer_error404.databinding.GoalsItemBinding
import kotlin.math.roundToInt

class GoalAdapter(val activity: GoalPage):
    RecyclerView.Adapter<GoalAdapter.ItemViewHolder>() {
    var goals = emptyList<Goal>()

    class ItemViewHolder(val binding: GoalsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalAdapter.ItemViewHolder {
        return ItemViewHolder(
            GoalsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GoalAdapter.ItemViewHolder, position: Int) {
        val goal = goals[position]
        holder.binding.apply {
            tvGoalItemTitle.text = goal.g_title //name of the entity column
            tvtotalTime.text = getTimeStringFromDouble(goal.g_time)

            if(goal.g_state == true.toString()) { //complete
                tvGoalItemTitle.paintFlags = tvGoalItemTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvGoalItemTitle.setTextColor(Color.GRAY)
            }
            else {
                tvGoalItemTitle.paintFlags = 0
                tvGoalItemTitle.setTextColor(Color.BLACK)
            }
            ivGoalItem.setOnClickListener{
                val intent = Intent(activity, GoalDetailsPage::class.java)
                intent.putExtra("id", goal.g_id)
                intent.putExtra("title", goal.g_title)
                intent.putExtra("description", goal.g_description)
                intent.putExtra("state", goal.g_state)
                intent.putExtra("time", goal.g_time)
                activity.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = goals.size

    fun updateRecycleView(new_goals: List<Goal>) {
        Log.d("TAG RV", "UPDATING RV")
        Log.d("TAG RV", "NEW RV: \n${new_goals.toString()}")
        this.goals = new_goals
        this.notifyDataSetChanged()
    }

    fun getGoalID(position: Int): Int {
        var goal = goals[position]
        return goal.g_id
    }

    fun getGoal(position: Int): Goal {
        var goal = goals[position]
        return goal
    }

    private fun getTimeStringFromDouble(time: Double): String
    {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return makeTimeString(hours,minutes,seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d",hour,min,sec)
}