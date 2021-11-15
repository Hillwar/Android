package com.hillwar.havigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hillwar.havigation.R
import com.hillwar.havigation.databinding.FragmentLeaderboardBinding

class Leaderboard : Fragment() {

    private var i = 0
    private lateinit var binding: FragmentLeaderboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)
        binding = FragmentLeaderboardBinding.inflate(layoutInflater)
        binding.butFragment.setOnClickListener {
            findNavController().navigate(R.id.action_leaderboard_self)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLeaderboardBinding.bind(view)
        i = LeaderboardArgs.fromBundle(requireArguments()).i + 1
        binding.butFragment.setOnClickListener {
            navigate(LeaderboardDirections.actionLeaderboardSelf(i))
        }
        binding.textFragment.text = i.toString()
    }
}