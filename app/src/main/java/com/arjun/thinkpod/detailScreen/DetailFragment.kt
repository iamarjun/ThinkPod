package com.arjun.thinkpod.detailScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val podcastListAdapter by lazy { PodcastListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        arg.channel.link?.let { viewModel.fetchFeed(it) }
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
            .load(arg.channel.image?.url)
            .into(binding.avatar)

        binding.title.text = arg.channel.title
        binding.author.text = arg.channel.title
        binding.description.text = arg.channel.description

        binding.podcasts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(EqualSpacingItemDecoration(8, EqualSpacingItemDecoration.HORIZONTAL))
            adapter = podcastListAdapter
        }

        viewModel.rssChannel.observe(viewLifecycleOwner, {

            binding.progress.isVisible = it is Resource.Loading

            when (it) {

                is Resource.Success -> {
                    Timber.d(it.data?.articles.toString())
                    it.data?.let { channel -> podcastListAdapter.submitList(channel.articles) }
                }
                is Resource.Error -> {
                    Timber.e(it.message)
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

    }
}