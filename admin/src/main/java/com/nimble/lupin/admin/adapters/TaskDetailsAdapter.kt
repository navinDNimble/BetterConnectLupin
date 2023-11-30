package com.nimble.lupin.admin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nimble.lupin.admin.databinding.ItemTaskUpdatesBinding
import com.nimble.lupin.admin.models.TaskUpdatesModel


class TaskDetailsAdapter(private var itemList: List<TaskUpdatesModel>) : RecyclerView.Adapter<TaskDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterItemMedicineBinding = ItemTaskUpdatesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(adapterItemMedicineBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)

    }

    fun updateList(newList: List<TaskUpdatesModel>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(itemList, newList))
        itemList = newList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val binding: ItemTaskUpdatesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskUpdatesModel, position: Int) {
            var pointer = 1
            if (checkNull(item.update_date)){

                binding.textViewDate.text = "Date : "+item.update_date.toString()
                binding.textViewDate.visibility  = View.VISIBLE

            }
            if (checkNull(item.male_count)){
                setTextOnView("Male Count : "+item.male_count.toString() , pointer)
                pointer++
            }
            if (checkNull(item.female_count)){
                setTextOnView("Female Count : "+item.female_count.toString() , pointer)
                pointer++
            }
            if (checkNull(item.demo_count)){
                setTextOnView("Demo Count : " + item.demo_count.toString() , pointer)
                pointer++
            }
            if (checkNull(item.event_count)){
                setTextOnView("Event Count : " + item.event_count.toString() , pointer)
                pointer++
            }
            if (checkNull(item.lg_code)){
                setTextOnView("LG Code : " + item.lg_code.toString() , pointer)
                pointer++
            }
            if (checkNull(item.wells_count)){
                setTextOnView("Wells Count : " + item.wells_count.toString() , pointer)
                pointer++
            }

            if (checkNull(item.survey_count)){
                setTextOnView("Survey Count : " + item.survey_count.toString() , pointer)
                pointer++
            }
            if (checkNull(item.village_count)){
                setTextOnView("Village Count : " + item.village_count.toString() , pointer)
                pointer++
            }
            if (checkNull(item.training_count)){
                setTextOnView("Training Count : " + item.training_count.toString() , pointer)
                pointer++
            }
            if (checkNull(item.no_of_farmers)){
                setTextOnView("No Of Farmers  : " + item.no_of_farmers.toString() , pointer)
                pointer++
            }
            if (checkNull(item.spinnerSelection)){
                setTextOnView("Type : " + item.spinnerSelection.toString() , pointer)
            }
            if (checkNull(item.findings)){
                setTextOnView("Findings : " + item.findings.toString() , 5)
            }
            if (checkNull(item.subject)){
                setTextOnView("Subject : " + item.subject.toString() , 5)
            }
            if (checkNull(item.reason)){
                setTextOnView("Reason : " + item.reason.toString() , 5)
            }
            if (checkNull(item.reasonForVisit)){
                setTextOnView("Reason For Visit : " + item.reasonForVisit.toString() , 5)
            }
            if (checkNull(item.meeting_with_whome)){
                setTextOnView("Meeting With : " + item.meeting_with_whome.toString() , 5)
            }
            if (checkNull(item.name_of_farmer)){
                setTextOnView("Name With Farmer : " + item.reason.toString() , 5)
            }


            //TODO :pHOTO SELECTING IS REMAINING

        }

        private fun checkNull( value : String?): Boolean {
            if (value != null){
                return true
            }
            return false
        }
        private fun checkNull( value : Int?): Boolean {
            if (value != null){
                return true
            }
            return false
        }
        private fun setTextOnView(toString: String , pointer  :Int) {
            when(pointer){
                1->{
                    binding.textView1Update.text = toString
                    binding.textView1Update.visibility = View.VISIBLE

                }
                2->{
                    binding.textView2Update.text = toString
                    binding.textView2Update.visibility = View.VISIBLE
                }
                3->{
                    binding.textView3Update.text = toString
                    binding.textView3Update.visibility = View.VISIBLE
                }
                4->{
                    binding.textView4Update.text = toString
                    binding.textView4Update.visibility = View.VISIBLE
                }
                5->{
                    binding.textView5Update.text = toString
                    binding.textView5Update.visibility = View.VISIBLE
                }
            }

        }
    }
    class TaskDiffCallback(private val oldList: List<TaskUpdatesModel>, private val newList: List<TaskUpdatesModel>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].taskUpdateId == newList[newItemPosition].taskUpdateId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}