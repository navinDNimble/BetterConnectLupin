package com.nimble.lupin.admin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nimble.lupin.admin.databinding.ItemBottomSheetBinding
import com.nimble.lupin.admin.databinding.ItemTaskBinding
import com.nimble.lupin.admin.interfaces.OnBottomSheetItemSelected
import com.nimble.lupin.admin.models.BottomSheetModel
import com.nimble.lupin.admin.models.TaskModel

class BottomAdapter (private val itemList  :List<BottomSheetModel> , private val onBottomSheetItemSelected: OnBottomSheetItemSelected  , private  val type :String): RecyclerView.Adapter<BottomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterItemMedicineBinding = ItemBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(adapterItemMedicineBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(itemList[position])
        holder.binding.root.setOnClickListener {
            onBottomSheetItemSelected.onBottomSheetItemSelected(itemList[position] , type)
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val binding: ItemBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BottomSheetModel) {
            binding.textViewAssignTaskTaskTitleIn.text = item.title
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
            return oldList[oldItemPosition].taskId == newList[newItemPosition].taskId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}