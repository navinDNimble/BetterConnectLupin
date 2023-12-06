package com.nimble.lupin.user.adapters

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.nimble.lupin.user.databinding.ItemTaskUpdatesBinding
import com.nimble.lupin.user.interfaces.OnClickSeePhoto
import com.nimble.lupin.user.models.TaskUpdatesModel

class TaskDetailsAdapter(private var itemList: List<TaskUpdatesModel> , private var  onClickSeePhoto: OnClickSeePhoto) : RecyclerView.Adapter<TaskDetailsAdapter.ViewHolder>() {

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


    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(val binding: ItemTaskUpdatesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskUpdatesModel, position: Int) {
            Log.d("sachin",item.toString())
            var pointer = 1
             if (checkNull(item.update_date)){
                 binding.textViewDate.visibility =  View.VISIBLE
                binding.textViewDate.text = "Date :  "+item.update_date
             }
             if (checkNull(item.male_count)){
                 setTextOnView("Male Count : "+item.male_count.toString() , pointer)
                 pointer++
             }
            if (checkNull(item.female_count)){
                setTextOnView("Female Count : "+item.female_count.toString() , pointer)
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

            if (checkNull(item.no_of_farmers)){
                setTextOnView("No Of Farmers  : " + item.no_of_farmers.toString() , pointer)
                pointer++
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
            if (item.photo ==1){
                binding.seePhotoId.visibility =View.VISIBLE
                binding.seePhotoId.setOnClickListener {
                   onClickSeePhoto.onClickSeePhoto(item.taskUpdateId!!)
                }
            }


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
}