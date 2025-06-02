package com.example.demo.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.demo.R
import com.example.demo.component.BaseActivity
import com.example.demo.databinding.ActivityMainBinding
import com.example.demo.fragment.ContactListFragment
import com.example.demo.fragment.ConversationListFragment
import com.example.demo.fragment.DiscoveryFragment
import com.example.demo.fragment.MeFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val mFragmentList = mutableListOf<Fragment>()
    private var contactListFragment: ContactListFragment? = null
    private var conversationListFragment: ConversationListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
//        navHostFragment.loadRoot(MainFragment::class)
        binding.startingTextView.setVisibility(View.GONE)
        binding.contentLinearLayout.setVisibility(View.VISIBLE)


        //设置ViewPager的最大缓存页面
        binding.contentViewPager.setOffscreenPageLimit(4)

        conversationListFragment = ConversationListFragment()
        contactListFragment = ContactListFragment()
        val discoveryFragment: DiscoveryFragment = DiscoveryFragment()
        val meFragment: MeFragment = MeFragment()
        mFragmentList.add(conversationListFragment!!)
        mFragmentList.add(contactListFragment!!)
//        val showWorkSpace = !TextUtils.isEmpty(Config.WORKSPACE_URL)
//        if (showWorkSpace) {
//            mFragmentList.add(WebViewFragment.loadUrl(Config.WORKSPACE_URL))
//        }
        mFragmentList.add(discoveryFragment)
        mFragmentList.add(meFragment)

        binding.contentViewPager.setAdapter(HomeFragmentPagerAdapter(this, mFragmentList))
        binding.contentViewPager.registerOnPageChangeCallback(this.onPageChangeCallback)

        binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.conversation_list -> {
                    setCurrentViewPagerItem(0, false)
                    setTitle(R.string.app_title_chat)
//                    if (!isDarkTheme()) {
//                        setTitleBackgroundResource(R.color.gray5, false)
//                    }
                }

                R.id.contact -> {
                    setCurrentViewPagerItem(1, false)
                    setTitle(R.string.app_title_contact)
//                    if (!isDarkTheme()) {
//                        setTitleBackgroundResource(R.color.gray5, false)
//                    }
                }

                R.id.discovery -> {
                    setCurrentViewPagerItem(2, false)
                    setTitle(R.string.app_title_discover)
//                    if (!isDarkTheme()) {
//                        setTitleBackgroundResource(R.color.gray5, false)
//                    }
                }

                R.id.me -> {
                    setCurrentViewPagerItem(3, false)
                    setTitle(R.string.app_title_me)
//                    if (!isDarkTheme()) {
//                        setTitleBackgroundResource(R.color.white, false)
//                    }
                }

                else -> {}
            }
            true
        })
    }

    private fun setCurrentViewPagerItem(item: Int, smoothScroll: Boolean) {
        if (binding.contentViewPager.currentItem != item) {
            binding.contentViewPager.setCurrentItem(item, smoothScroll)
        }
    }

    private val onPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            when (position) {
                0 -> binding.bottomNavigationView.setSelectedItemId(R.id.conversation_list)
                1 -> binding.bottomNavigationView.setSelectedItemId(R.id.contact)
                2 -> binding.bottomNavigationView.setSelectedItemId(R.id.discovery)
                3 -> binding.bottomNavigationView.setSelectedItemId(R.id.me)
                else -> {}
            }
//            contactListFragment.showQuickIndexBar(position == 1)
        }

        override fun onPageScrollStateChanged(state: Int) {
//            if (state != ViewPager.SCROLL_STATE_IDLE) {
//                //滚动过程中隐藏快速导航条
//                contactListFragment.showQuickIndexBar(false)
//            } else {
//                contactListFragment.showQuickIndexBar(true)
//            }
        }
    }
}