package com.montoto.inventapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.montoto.inventapp.data.models.ItemResponse
import com.montoto.inventapp.databinding.LayoutItemArticulosBinding
import com.montoto.inventapp.domain.model.ItemModel

class ArticlesAdapter(private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    private var articles: List<ItemModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutItemArticulosBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position], itemClickListener, position)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ViewHolder(private val binding: LayoutItemArticulosBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemModel, itemClickListener: OnItemClickListener, position: Int) {
            binding.txtIdArticle.text = item.idArticle
            binding.txtDataTop.text = item.clientName
            binding.txtDataBottom.text = "Fecha de Alta: " + item.startDate
            binding.txtDataBottomTwo.text = "Artículo: " + item.article

            itemView.setOnClickListener { itemClickListener.onItemClick(item, position) }
            itemView.setOnLongClickListener {
                itemClickListener.onItemLongClick(item, position)
                true
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(article: ItemModel?, position: Int)
        fun onItemLongClick(article: ItemModel?, position: Int) {
            //Implement if it is needed.
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(input: List<ItemModel>) {
        articles = input
        notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "ArticlesAdapter"
    }
}