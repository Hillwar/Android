package com.hillwar.web

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hillwar.web.databinding.ListItemBinding

class ImageAdapter(private val images: List<Image>, private val onClick : (Image) -> Unit) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        private var binding: ListItemBinding = ListItemBinding.bind(root)
        val author: TextView = binding.author
        val photo: ImageView = binding.photo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val holder = ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
        holder.root.setOnClickListener{
            onClick(images[holder.adapterPosition])}
        return holder
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val current = images[position]
        with(holder) {
            author.text = current.author
            if (current.photo != null) photo.setImageBitmap(current.photo)
        }
    }
}