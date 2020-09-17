package com.arjun.thinkpod.detailScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.arjun.thinkpod.GlideApp
import com.arjun.thinkpod.R
import com.arjun.thinkpod.databinding.FragmentDetailBinding
import com.arjun.thinkpod.util.viewBinding

class DetailFragment : Fragment() {

    private val binding: FragmentDetailBinding by viewBinding(FragmentDetailBinding::bind)
    private val arg: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
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

    }
}