package com.example.demo.fragment

import android.os.Bundle
import android.view.View
import com.huaxia.common.base.BaseFragment
import com.example.demo.databinding.FragmentThirdBinding

class DiscoveryFragment: BaseFragment<FragmentThirdBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.title = "ThirdFragment"
//        binding.btnMain.setOnClickListener {
//            navigator.popTo(ConversationListFragment::class)
//        }
//        CoroutineHelper.runOnIO {
//            LogUtils.e("runOnMain")
//        }
    }
}