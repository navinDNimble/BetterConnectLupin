package com.nimble.lupin.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nimble.lupin.admin.databinding.ItemTaskBinding
import com.nimble.lupin.admin.interfaces.OnTaskSelected
import com.nimble.lupin.admin.models.TaskModel

class TaskAdapter(private var itemList: List<TaskModel>, private val onTaskSelected: OnTaskSelected) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterItemMedicineBinding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(adapterItemMedicineBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    fun updateList(newList: List<TaskModel>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(itemList, newList))
        itemList = newList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskModel, position: Int) {
            binding.textViewActivityNameIn.text = item.activityName
        }
    }
    class TaskDiffCallback(private val oldList: List<TaskModel>, private val newList: List<TaskModel>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}