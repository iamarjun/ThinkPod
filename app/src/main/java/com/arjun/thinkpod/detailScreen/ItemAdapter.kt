package com.arjun.thinkpod.detailScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arjun.thinkpod.GlideApp
import com.arjun.thinkpod.R
import com.arjun.thinkpod.databinding.PodcastItemBinding
import com.arjun.thinkpod.model.xml.Item
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class ItemAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    private val ogList: MutableList<Item> = ArrayList()
    private val filterList: MutableList<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.podcast_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Item>) {
        ogList.clear()
        ogList.addAll(list)
        filterList.clear()
        filterList.addAll(list)
        differ.submitList(list)
    }

    class ItemViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = PodcastItemBinding.bind(itemView)

        fun bind(item: Item) {
            binding.root.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            GlideApp.with(itemView)
                .load(if (item.itemImages != null) item.itemImages[0].itemImageHref else R.drawable.podcast_placeholder)
                .into(binding.image)

            binding.description.text = item.description
            binding.title.text = item.title
            binding.date.text = convertDate(item.pubDate)
            binding.duration.text = item.iTunesDuration
        }

        private fun convertDate(date: String?): String {
            val dateTimeFormatterBuilder = DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern("" + "[EEE, d MMM yyyy HH:mm:ss z]" + "[E, d MMM yyyy HH:mm:ss Z]"))

            val df = dateTimeFormatterBuilder.toFormatter()
            val time = LocalDateTime.parse(date, df)
                .format(DateTimeFormatter.ofPattern("E, dd MMM"))
            Timber.d(time)
            return time
        }
    }

    override fun getFilter(): Filter = PodcastFilter()

    inner class PodcastFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            val query = constraint.toString().trim()
            if (query.isNotEmpty()) {
                val list = ogList.filter { item ->
                    item.title.contains(
                        query,
                        ignoreCase = true
                    )
                }
                filterList.clear()
                filterList.addAll(list)
            }
            return filterResults.apply {
                count = filterList.size
                values = filterList
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.let {
                differ.submitList(it.values as MutableList<Item>)
                notifyDataSetChanged()
            }
        }

    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Item)
    }
}

