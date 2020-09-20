package com.arjun.thinkpod.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arjun.thinkpod.GlideApp
import com.arjun.thinkpod.R
import com.arjun.thinkpod.databinding.ChannelItemBinding
import com.arjun.thinkpod.model.Podcast

class PodcastAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Podcast>() {

        override fun areItemsTheSame(oldItem: Podcast, newItem: Podcast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Podcast, newItem: Podcast): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return PodcastViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.channel_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PodcastViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Podcast>) {
        differ.submitList(list)
    }

    class PodcastViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ChannelItemBinding.bind(itemView)

        fun bind(item: Podcast) {

            binding.root.setOnClickListener {
                interaction?.onItemSelected(binding.avatar, item)
            }

            binding.title.text = item.name

            binding.avatar.apply {
                transitionName = item.id
                GlideApp.with(itemView)
                    .load(item.artworkUrl)
                    .into(this)
            }

        }
    }

    interface Interaction {
        fun onItemSelected(view: View, item: Podcast)
    }
}

