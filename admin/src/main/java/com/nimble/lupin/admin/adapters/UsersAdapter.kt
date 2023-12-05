package com.nimble.lupin.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nimble.lupin.admin.databinding.ItemUserListBinding
import com.nimble.lupin.admin.interfaces.OnUserSelected
import com.nimble.lupin.admin.models.UserModel

class UsersAdapter(private var itemList: List<UserModel>, private val onUsersSelected: OnUserSelected ) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val userListItem = ItemUserListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(userListItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
        holder.binding.root.setOnClickListener {
            onUsersSelected.onUserSelected(itemList[position])
        }
    }

    fun updateList(newList: List<UserModel>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(itemList, newList))
        itemList = newList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserModel, position: Int) {
            binding.textViewMultiUsername.text = item.firstName + " " + item.lastName
            Glide.with(binding.root).load(item.profilePhoto).into(binding.imageViewProfileImage)
        }
    }
    class TaskDiffCallback(private val oldList: List<UserModel>, private val newList: List<UserModel>) : DiffUtil.Callback() {
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