package com.coopera.todoappproject.fragments.list

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.coopera.todoappproject.R
import com.coopera.todoappproject.data.models.Priority
import com.coopera.todoappproject.data.models.ToDoData

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todoData = dataList[position]
        val priority = dataList[position].priority

        holder.textViewTitle.text = todoData.title
        holder.textViewDescription.text = todoData.description

        val priorityIndicator = holder.priorityIndicator
        when(priority) {
            Priority.HIGH -> priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.red)
            )
            Priority.MEDIUM -> priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.yellow)
            )
            Priority.LOW -> priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.green)
            )
        }
        holder.cardItem.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(todoData)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(toDoData: List<ToDoData>) {
        dataList = toDoData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.title_text)
        val textViewDescription: TextView = itemView.findViewById(R.id.description_text)
        val priorityIndicator: CardView = itemView.findViewById(R.id.priority_indicator)
        val cardItem: ConstraintLayout = itemView.findViewById(R.id.row_background)
    }

}