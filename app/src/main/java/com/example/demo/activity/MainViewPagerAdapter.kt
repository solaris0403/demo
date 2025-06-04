package com.example.demo.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.demo.fragment.ContactListFragment
import com.example.demo.fragment.ConversationListFragment
import com.example.demo.fragment.DiscoveryFragment
import com.example.demo.fragment.MeFragment

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return ConversationListFragment()
            }

            1 -> {
                return ContactListFragment()
            }

            2 -> {
                return DiscoveryFragment()
            }

            3 -> {
                return MeFragment()
            }

            else -> {
                throw IndexOutOfBoundsException("Invalid position $position")
            }
        }
    }
}