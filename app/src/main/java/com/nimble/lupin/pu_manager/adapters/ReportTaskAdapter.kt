package com.nimble.lupin.pu_manager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nimble.lupin.pu_manager.databinding.ItemTaskBinding
import com.nimble.lupin.pu_manager.interfaces.OnTaskSelected
import com.nimble.lupin.pu_manager.models.TaskModel
import com.nimble.lupin.pu_manager.utils.MyDiffUtil


class ReportTaskAdapter(private val onTaskSelected: OnTaskSelected) : RecyclerView.Adapter<ReportTaskAdapter.ViewHolder>() {
      var itemList: MutableList<TaskModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterItemMedicineBinding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(adapterItemMedicineBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("sachinOnBindingViewHolder","caleed")
        holder.bind(itemList[position], position)
        holder.binding.root.setOnClickListener {
            onTaskSelected.onTaskSelected(itemList[position] )
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskModel, position: Int) {
            binding.textViewAssignTaskTaskTitleIn.text = item.taskId.toString()+" " +item.taskName
            binding.textViewActivityNameIn.text = item.activityName +" - "+ item.subActivityName
            binding.textViewAssignTaskStartDateIn.text ="${item.startDate} ${" To "+item.endDate}"
            binding.units.visibility = View.GONE
        }
    }

}