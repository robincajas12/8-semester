package com.mobiledev.firstapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobiledev.firstapp.databinding.Fragment2Binding

class Fragment2 : Fragment() {

    // Binding solo válido entre onCreateView y onDestroyView
    private var _binding: Fragment2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Fragment2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFragment2.text = "Fragment 2"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
