package com.example.demo.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.demo.vm.MainVM
import com.huaxia.common.base.BaseFragment
import com.example.demo.databinding.FragmentMainBinding

class ConversationListFragment: BaseFragment<FragmentMainBinding>() {
    private val viewModel: MainVM by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.title = "MainFragment"
//        viewModel.getMode().observe(viewLifecycleOwner){
//            LogUtils.e(it)
//        }
//
//        binding.btnMain.setOnClickListener {
//            navigator.push(ContactListFragment::class){
//                arguments = Bundle().apply {
//                    putString("123", binding.edtA.text.toString())
//                }
//            }
//        }
    }
}