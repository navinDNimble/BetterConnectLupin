package com.nimble.lupin.pu_manager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nimble.lupin.pu_manager.databinding.ItemTasksAllocatedToUserBinding
import com.nimble.lupin.pu_manager.interfaces.OnUserTaskSelected
import com.nimble.lupin.pu_manager.models.UserTasksListModel

class UserTasksAdapter(private var itemList: List<UserTasksListModel> , private val onUserTaskSelected:  OnUserTaskSelected) : RecyclerView.Adapter<UserTasksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterItemMedicineBinding = ItemTasksAllocatedToUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(adapterItemMedicineBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
        holder.binding.root.setOnClickListener {
            onUserTaskSelected.onUserTaskSelected(itemList[position])
        }

    }

    fun updateList(newList: List<UserTasksListModel>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(itemList, newList))
        itemList = newList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val binding: ItemTasksAllocatedToUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserTasksListModel, position: Int) {
            binding.textViewAssignTaskTaskTitleIn.text =  item.task.taskId.toString()+" "+item.task?.taskName
            binding.textViewActivityNameIn.text = item.task?.activityName +" - "+ item.task?.subActivityName

            binding.textViewAssignTaskStartDateIn.text ="${item.task?.startDate} ${" To "+item.task?.endDate}"
            binding.units.text = "${item.userTask?.completedUnit} ${"/"+item.userTask?.totalUnits}"

        }
    }
    class TaskDiffCallback(private val oldList: List<UserTasksListModel>, private val newList: List<UserTasksListModel>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].userTask.userTaskId == newList[newItemPosition].userTask.userTaskId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}