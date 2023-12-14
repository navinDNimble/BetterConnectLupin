package com.nimble.lupin.pu_manager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nimble.lupin.pu_manager.databinding.ItemUserAllocatedToTaskBinding
import com.nimble.lupin.pu_manager.interfaces.OnTaskUserSelected

import com.nimble.lupin.pu_manager.models.TaskUsersModel

class TaskUsersAdapter(private var itemList: List<TaskUsersModel> , private val onTaskUserSelected: OnTaskUserSelected): RecyclerView.Adapter<TaskUsersAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val userView= ItemUserAllocatedToTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(userView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(itemList[position],position)
        holder.binding.root.setOnClickListener {
            onTaskUserSelected.onTaskUserSelected(itemList[position])
        }
    }
    fun updateList(newList: List<TaskUsersModel>) {
        Log.d("sachinnewlis",newList.toString())
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(itemList, newList))
        itemList = newList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }


    class ViewHolder(val binding: ItemUserAllocatedToTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskUsersModel, position: Int) {
            binding.textViewUsername.text = item.user?.firstName + " " + (item.user?.lastName )
            binding.unitsUser.text = item.userTask?.completedUnit.toString() + " / " + item.userTask?.totalUnits.toString()
            Glide.with(binding.root).load(item.user?.profilePhoto).into(binding.imageViewProfileImage)
        }
    }
    class TaskDiffCallback(private val oldList: List<TaskUsersModel>, private val newList: List<TaskUsersModel>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].user?.userId == newList[newItemPosition].user?.userId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}