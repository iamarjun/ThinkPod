package com.arjun.thinkpod.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.thinkpod.R
import com.arjun.thinkpod.databinding.FragmentHomeBinding
import com.arjun.thinkpod.model.Channels
import com.arjun.thinkpod.util.EqualSpacingItemDecoration
import com.arjun.thinkpod.util.viewBinding
import com.prof.rssparser.Channel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val channelListAdapter by lazy {
        ChannelListAdapter(object : ChannelListAdapter.Interaction {
            override fun onItemSelected(view: View, item: Channel) {
                val extras = FragmentNavigatorExtras(
                    view to item.image?.link!!
                )
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(item)
                findNavController().navigate(action, extras)
            }
        })
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

        channelListAdapter.submitList(Channels.channelList)

        binding.podcastChannels.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
            addItemDecoration(EqualSpacingItemDecoration(64, EqualSpacingItemDecoration.GRID))
            adapter = channelListAdapter
        }
    }

}