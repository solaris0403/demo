package com.example.demo.fragment

import android.os.Bundle
import android.view.View
import com.example.demo.component.BaseFragment
import com.example.demo.databinding.FragmentMeBinding
import com.huaxia.xlib.coroutine.CoroutineHelper
import com.example.demo.databinding.FragmentThirdBinding
import com.github.fragivity.navigator
import com.github.fragivity.popTo
import com.huaxia.xlib.log.LogUtils

class MeFragment: BaseFragment<FragmentMeBinding>() {
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