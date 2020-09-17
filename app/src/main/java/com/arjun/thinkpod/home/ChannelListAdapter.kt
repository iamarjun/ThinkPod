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
import com.prof.rssparser.Channel

class ChannelListAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Channel>() {

        override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ChannelViewHolder(
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
            is ChannelViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Channel>) {
        differ.submitList(list)
    }

    class ChannelViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ChannelItemBinding.bind(itemView)

        fun bind(item: Channel) {

            binding.root.setOnClickListener {
                interaction?.onItemSelected(binding.avatar, item)
            }

            binding.title.text = item.title

            binding.avatar.apply {
                transitionName = item.image?.url
                GlideApp.with(itemView)
                    .load(item.image?.url)
                    .into(this)
            }

        }
    }

    interface Interaction {
        fun onItemSelected(view: View, item: Channel)
    }
}

