package com.nimble.lupin.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nimble.lupin.admin.databinding.ItemMultipleUserSelectionBinding
import com.nimble.lupin.admin.models.AssignTaskModel
import com.nimble.lupin.admin.views.home.schedule.assignTask.AssignTaskFragment


class UsersSelectionAdapter(private var itemList: List<AssignTaskModel> ) : RecyclerView.Adapter<UsersSelectionAdapter.ViewHolder>() {

    private var selectedUserList = mutableSetOf<AssignTaskModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterItemMedicineBinding = ItemMultipleUserSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(adapterItemMedicineBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = itemList[position]
        holder.binding.viewModel = model
        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                selectedUserList.add(model)
            }else {
                 removeItemFromSelectedList(model.userId)
            }

        }
        holder.binding.editTextUnits.addTextChangedListener {
            model.total_units = it.toString()
        }

    }
    private fun removeItemFromSelectedList(userId : Int) {
        selectedUserList.removeIf { it.userId == userId}
        AssignTaskFragment().selectedItemList.removeIf { it.userId == userId}
    }

     fun getSelectedList(): MutableSet<AssignTaskModel> {
        return selectedUserList
    }
    fun updateList(newList: List<AssignTaskModel>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(itemList, newList))
        itemList = newList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(val binding: ItemMultipleUserSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
    class TaskDiffCallback(private val oldList: List<AssignTaskModel>, private val newList: List<AssignTaskModel>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].userId == newList[newItemPosition].userId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}