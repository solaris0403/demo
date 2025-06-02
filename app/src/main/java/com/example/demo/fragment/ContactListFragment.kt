package com.example.demo.fragment

import android.os.Bundle
import android.view.View
import com.example.demo.component.BaseFragment
import com.example.demo.databinding.FragmentSecondBinding
import com.github.fragivity.navigator
import com.github.fragivity.push

class ContactListFragment: BaseFragment<FragmentSecondBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.title = "SecondFragment"
//        binding.btnMain.setOnClickListener {
//            navigator.push(DiscoveryFragment::class)
//        }
//        val key = arguments?.getString("123")
//        binding.edtA.setText(key.toString())
    }
}