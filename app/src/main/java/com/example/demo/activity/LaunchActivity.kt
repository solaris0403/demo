package com.example.demo.activity

import android.R
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.core.app.ActivityOptionsCompat
import com.example.demo.KeyStoreUtil
import com.example.demo.component.BaseActivity
import com.example.demo.databinding.ActivityLaunchBinding
import com.huaxia.xlib.sp.SpUtils
import com.huaxia.xlib.utils.SPUtils
import com.tencent.mmkv.MMKV

class LaunchActivity: BaseActivity<ActivityLaunchBinding>() {

    private var id: String? = ""
    private var token: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
//            id = KeyStoreUtil.getData(this, "wf_userId")
//            token = KeyStoreUtil.getData(this, "wf_token")
//            id = SpUtils.getInstance().getString("wf_userId")
//            token = SpUtils.getInstance().getString("wf_token")

            id = MMKV.defaultMMKV().getString("wf_userId", "")
            token = MMKV.defaultMMKV().getString("wf_token", "")

        } catch (e: Exception) {
            e.printStackTrace()
        }
        Handler().postDelayed({ this.showNextScreen() }, 1000)
    }

    private fun showNextScreen(){
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(token)) {
            showMain()
        } else {
            showLogin()
        }
    }

    private fun showMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val bundle = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out).toBundle()
        startActivity(intent, bundle)
        finish()
    }

    private fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("isKickedOff", getIntent().getBooleanExtra("isKickedOff", false))
        val bundle = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out).toBundle()
        startActivity(intent, bundle)
        finish()
    }
}