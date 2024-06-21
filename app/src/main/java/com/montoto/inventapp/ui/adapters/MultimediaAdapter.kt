package com.montoto.inventapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.montoto.inventapp.databinding.LayoutItemAddArticlesBinding
import com.montoto.inventapp.domain.model.FileImageModel
import com.montoto.inventapp.ui.screens.AddArticleFragment
import com.montoto.inventapp.ui.screens.ItemDetailFragment
import com.montoto.inventapp.util.Constants.Inventory.Url.FULL_URL

class MultimediaAdapter(
    private val filesPath: List<FileImageModel>,
    private val fragment: Fragment,
    listener: OnLongClickListener?
) : RecyclerView.Adapter<MultimediaAdapter.ViewHolder>() {

    private val itemClickListener: OnLongClickListener?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutItemAddArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filesPath[position], itemClickListener, position)
    }

    override fun getItemCount(): Int {
        return filesPath.size
    }

    inner class ViewHolder(private val binding: LayoutItemAddArticlesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(file: FileImageModel, itemClickListener: OnLongClickListener?, position: Int) {
            if (fragment is ItemDetailFragment) {
                Glide.with(binding.imgFiles)
                    .asBitmap()
                    .load(FULL_URL + file.path)
                    .into(binding.imgFiles)
            }
            if (fragment is AddArticleFragment) {
                Glide.with(binding.imgFiles)
                    .asBitmap()
                    .load(file.path)
                    .into(binding.imgFiles)
            }
            binding.imgFiles.setOnLongClickListener {
                itemClickListener!!.onItemClick(file, absoluteAdapterPosition)
                false
            }
        }
    }

    interface OnLongClickListener {
        fun onItemClick(file: FileImageModel?, position: Int)
    }

    companion object {
        private const val TAG = "MultimediaAdapter"
    }

    init {
        itemClickListener = listener
    }
}