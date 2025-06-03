package com.example.demo.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.core.app.ActivityOptionsCompat
import com.huaxia.common.base.BaseActivity
import com.example.demo.databinding.ActivityLaunchBinding
import com.huaxia.common.ext.launch
import com.huaxia.common.mmkv.MMKVUtil
import kotlinx.coroutines.delay

class LaunchActivity : BaseActivity<ActivityLaunchBinding>() {

    private var id: String? = ""
    private var token: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            id = MMKVUtil.getString("wf_userId")
            token = MMKVUtil.getString("wf_token")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        launch {
            delay(1000)
            showNextScreen()
        }
    }

    private fun showNextScreen() {
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(token)) {
            showMain()
        } else {
//            showLogin()
            showMain()
        }
    }

    private fun showMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val bundle = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle()
        startActivity(intent, bundle)
        finish()
    }

    private fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("isKickedOff", getIntent().getBooleanExtra("isKickedOff", false))
        val bundle = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle()
        startActivity(intent, bundle)
        finish()
    }
}