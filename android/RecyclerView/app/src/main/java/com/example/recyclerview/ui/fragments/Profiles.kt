package com.example.recyclerview.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.databinding.FragmentProfilesBinding
import com.example.recyclerview.logic.users.GetAllUsers
import com.example.recyclerview.ui.adapters.UserAdapter

class Profiles : Fragment() {

    private var _binding: FragmentProfilesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val userAdapter = UserAdapter().apply {
            userList = GetAllUsers()()
        }

        binding.myRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.myRecyclerView.adapter = userAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
