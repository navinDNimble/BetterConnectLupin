package com.nimble.lupin.user.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.nimble.lupin.user.databinding.ItemUploadPhotoBinding
import com.nimble.lupin.user.interfaces.OnImageUnselected




class ImageSelectorAdapter(private var itemList: List<Bitmap>, private val onImageUnselected: OnImageUnselected) : RecyclerView.Adapter<ImageSelectorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterItemMedicineBinding = ItemUploadPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(adapterItemMedicineBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
        holder.binding.unselectImage.setOnClickListener {

            onImageUnselected.onImageUnselected(position)

        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val binding: ItemUploadPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Bitmap) {
           Glide.with(binding.root).load(item).into(binding.imageviewId)
        }
    }

}