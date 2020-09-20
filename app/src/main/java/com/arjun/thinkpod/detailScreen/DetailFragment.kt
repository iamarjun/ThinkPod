package com.arjun.thinkpod.detailScreen

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.arjun.thinkpod.GlideApp
import com.arjun.thinkpod.MainViewModel
import com.arjun.thinkpod.R
import com.arjun.thinkpod.databinding.FragmentDetailBinding
import com.arjun.thinkpod.model.xml.Item
import com.arjun.thinkpod.service.PodcastActivity
import com.arjun.thinkpod.util.EqualSpacingItemDecoration
import com.arjun.thinkpod.util.Resource
import com.arjun.thinkpod.util.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val binding: FragmentDetailBinding by viewBinding(FragmentDetailBinding::bind)
    private val arg: DetailFragmentArgs by navArgs()
    private val viewModel: MainViewModel by viewModels()
    private val podcast by lazy { arg.podcast }
    private val itemAdapter by lazy {
        ItemAdapter(object : ItemAdapter.Interaction {
            override fun onItemSelected(position: Int, item: Item) {
                PodcastActivity.instance(requireContext(), item).also {
                    startActivity(it)
                }
            }

            override fun onShareSelected(position: Int, item: Item) {
                Timber.d("$position")
                shareItem(item)
            }

            override fun setFavourite(position: Int, item: Item) {
                Timber.d("$position")
            }
        })
    }

    private fun shareItem(item: Item) {
        val shareText = "Checkout: " + podcast.artistName + " " +
                item.title + " " +
                item.enclosures[0].url
        // Create a share intent
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setType(SHARE_INTENT_TYPE_TEXT)

            .setText(shareText)
            .setChooserTitle(getString(R.string.share_episode))
            .createChooserIntent()
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        startActivity(shareIntent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        viewModel.fetchRssFeedUrl(podcast.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlideApp.with(this)
            .load(podcast.artworkUrl)
            .into(binding.avatar)

        binding.title.text = podcast.name
        binding.author.text = podcast.artistName

        binding.podcasts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(EqualSpacingItemDecoration(8, EqualSpacingItemDecoration.HORIZONTAL))
            adapter = itemAdapter
        }

        viewModel.rssFeed.observe(viewLifecycleOwner, {

            binding.progress.isVisible = it is Resource.Loading

            when (it) {

                is Resource.Success -> {
                    Timber.d(it.data?.channel?.itemList?.toString())
                    binding.description.text = it.data?.channel?.description
                    it.data?.channel?.itemList?.let { itemList -> itemAdapter.submitList(itemList) }
                }
                is Resource.Error -> {
                    Timber.e(it.message)
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Timber.d("beforeTextChanged")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Timber.d("onTextChanged")
                itemAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                Timber.d("afterTextChanged")
                itemAdapter.filter.filter(s)
            }

        })
    }

    companion object {
        private const val SHARE_INTENT_TYPE_TEXT = "text/plain"
    }
}