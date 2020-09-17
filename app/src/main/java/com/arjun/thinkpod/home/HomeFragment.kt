package com.arjun.thinkpod.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.thinkpod.R
import com.arjun.thinkpod.databinding.FragmentHomeBinding
import com.arjun.thinkpod.model.Channels
import com.arjun.thinkpod.util.EqualSpacingItemDecoration
import com.arjun.thinkpod.util.viewBinding

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val channelListAdapter by lazy { ChannelListAdapter() }

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