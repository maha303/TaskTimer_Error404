package com.example.tasktimer_error404.adapters
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktimer_error404.MainActivity
import com.example.tasktimer_error404.database.Goal
import com.example.tasktimer_error404.databinding.GoalsItemBinding

class GoalAdapter(val activity: MainActivity):
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
//            goalTextView.text = goal.g_title //name of the entity column
//            Log.d("TAG RV", "tv text is: ${goalTextView.text}")
//            llGoalRV.setOnClickListener {
//                activity.updateSelectedGoal(goal)
//            }
        }
    }

    override fun getItemCount() = goals.size

    fun updateRecycleView(new_goals: List<Goal>) {
        Log.d("TAG RV", "UPDATING RV")
        Log.d("TAG RV", "NEW RV: \n${new_goals.toString()}")
        this.goals = new_goals
        this.notifyDataSetChanged()
    }
}