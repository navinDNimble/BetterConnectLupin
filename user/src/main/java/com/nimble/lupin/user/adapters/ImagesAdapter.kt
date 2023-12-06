package com.nimble.lupin.user.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.nimble.lupin.user.R
import com.nimble.lupin.user.databinding.ItemShowPhotoBinding


class ImagesAdapter(private var itemList: List<String>) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterItemMedicineBinding = ItemShowPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(adapterItemMedicineBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val binding: ItemShowPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            Glide.with(binding.root)
                .load(item)
                .placeholder(R.drawable.icon_gallery)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("TAG", "onLoadFailed")
                        binding.progressBarId.visibility =View.GONE

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                       binding.progressBarId.visibility =View.GONE
                        return false
                    }

                }).into(binding.imageviewId)

        }
    }

}