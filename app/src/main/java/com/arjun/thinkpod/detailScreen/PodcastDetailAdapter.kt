//package com.arjun.thinkpod.detailScreen
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.AsyncListDiffer
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.arjun.thinkpod.R
//import com.arjun.thinkpod.databinding.PodcastItemBinding
//import timber.log.Timber
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//
//class PodcastDetailAdapter(private val interaction: Interaction? = null) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
//
//        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return oldItem.guid == newItem.guid
//        }
//
//        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return oldItem == newItem
//        }
//
//    }
//    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//
//        return PodcastViewHolder(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.podcast_item,
//                parent,
//                false
//            ),
//            interaction
//        )
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is PodcastViewHolder -> {
//                holder.bind(differ.currentList.get(position))
//            }
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return differ.currentList.size
//    }
//
//    fun submitList(list: List<Article>) {
//        differ.submitList(list)
//    }
//
//    class PodcastViewHolder
//    constructor(
//        itemView: View,
//        private val interaction: Interaction?
//    ) : RecyclerView.ViewHolder(itemView) {
//
//        private val binding = PodcastItemBinding.bind(itemView)
//
//        fun bind(item: Article) {
//            binding.root.setOnClickListener {
//                interaction?.onItemSelected(adapterPosition, item)
//            }
//
//            binding.title.text = item.title
//            binding.date.text = convertDate(item.pubDate)
//        }
//
//        private fun convertDate(date: String?): String {
//            val df = DateTimeFormatter.ofPattern("E, d MMM yyyy HH:mm:ss Z")
//            val time = LocalDateTime.parse(date, df)
//                .format(DateTimeFormatter.ofPattern("E, dd MMM"))
//            Timber.d(time)
//            return time
//        }
//    }
//
//    interface Interaction {
//        fun onItemSelected(position: Int, item: Article)
//    }
//}
//
