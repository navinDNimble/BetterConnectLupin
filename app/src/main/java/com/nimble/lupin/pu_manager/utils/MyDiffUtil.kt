package com.nimble.lupin.pu_manager.utils

import androidx.recyclerview.widget.DiffUtil
import com.nimble.lupin.pu_manager.models.TaskModel
import javax.annotation.Nullable

class MyDiffUtil(
    private val oldList: List<TaskModel>,
    private val newList: List<TaskModel>,
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
   return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].taskId == newList[newItemPosition].taskId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}