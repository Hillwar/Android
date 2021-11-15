package com.hillwar.havigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hillwar.havigation.R
import com.hillwar.havigation.databinding.FragmentTitleBinding

class Title : Fragment() {
    private var i = 0
    private lateinit var binding: FragmentTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        i = LeaderboardArgs.fromBundle(requireArguments()).i + 1
        val binding = FragmentTitleBinding.bind(view)
        binding.butFragment.setOnClickListener {
            navigate(TitleDirections.actionTitleScreenSelf(i))
        }
        binding.textFragment.text = i.toString()
    }
}