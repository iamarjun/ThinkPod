package com.arjun.thinkpod.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.thinkpod.MainViewModel
import com.arjun.thinkpod.R
import com.arjun.thinkpod.databinding.FragmentHomeBinding
import com.arjun.thinkpod.model.Podcast
import com.arjun.thinkpod.util.EqualSpacingItemDecoration
import com.arjun.thinkpod.util.Resource
import com.arjun.thinkpod.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    private val podcastAdapter by lazy {
        PodcastAdapter(object : PodcastAdapter.Interaction {
            override fun onItemSelected(view: View, item: Podcast) {
                val extras = FragmentNavigatorExtras(
                    view to item.artistUrl
                )
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(item)
                findNavController().navigate(action, extras)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchTopPodcast()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.podcasts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
            addItemDecoration(EqualSpacingItemDecoration(64, EqualSpacingItemDecoration.GRID))
            adapter = podcastAdapter
        }

        viewModel.iTunesResponse.observe(viewLifecycleOwner, {

            binding.loader.isVisible = it is Resource.Loading

            when (it) {
                is Resource.Success -> {
                    Timber.d(it.data.toString())
                    it.data?.feed?.let { feed -> podcastAdapter.submitList(feed.podCasts) }
                }
                is Resource.Error -> {
                    Timber.e(it.message)
                }
            }
        })


    }

}