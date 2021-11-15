package com.hillwar.havigation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hillwar.havigation.R
import com.hillwar.havigation.databinding.FragmentRegisterBinding

class Register : Fragment() {

    private var i = 0
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        binding.butFragment.setOnClickListener {
            findNavController().navigate(R.id.action_register_self)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRegisterBinding.bind(view)
        i = LeaderboardArgs.fromBundle(requireArguments()).i + 1
        binding.butFragment.setOnClickListener {
            navigate(RegisterDirections.actionRegisterSelf(i))
        }
        binding.textFragment.text = i.toString()
    }
}