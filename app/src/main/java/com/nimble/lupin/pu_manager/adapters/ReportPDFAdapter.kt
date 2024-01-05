package com.nimble.lupin.pu_manager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nimble.lupin.pu_manager.databinding.ItemReportsPdfBinding
import com.nimble.lupin.pu_manager.models.TaskModel


class ReportPDFAdapter : RecyclerView.Adapter<ReportPDFAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportPDFAdapter.ViewHolder {
        val reportsPdfBinding = ItemReportsPdfBinding.inflate(LayoutInflater.from(parent.context),
        parent, false)
        return ViewHolder(reportsPdfBinding)
    }

    override fun onBindViewHolder(holder: ReportPDFAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class ViewHolder(val binding: ItemReportsPdfBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskModel, position: Int) {

        }
    }
}