package com.example.demo.activity

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.example.demo.AppRepo
import com.example.demo.DeviceMgr
import com.example.demo.KeyStoreUtil
import com.example.demo.SimpleTextWatcher
import com.huaxia.common.base.BaseActivity
import com.example.demo.databinding.ActivityLoginBinding
import com.example.demo.network.ApiResult
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.getBooleanExtra("isKickedOff", false)) {

        }

        binding.phoneNumberEditText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                inputAccount(s)
            }
        })
        binding.passwordEditText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                inputPassword(s)
            }
        })
        binding.authCodeLoginTextView.setOnClickListener {
            authCodeLogin()
        }
        binding.registerTextView.setOnClickListener {
            register()
        }
        binding.loginButton.setOnClickListener {
            login()
        }
    }

    fun inputAccount(editable: Editable?) {
        if (!TextUtils.isEmpty(binding.passwordEditText.text) && !TextUtils.isEmpty(editable)) {
            binding.loginButton.setEnabled(true)
        } else {
            binding.loginButton.setEnabled(false)
        }
    }

    fun inputPassword(editable: Editable?) {
        if (!TextUtils.isEmpty(binding.phoneNumberEditText.text) && !TextUtils.isEmpty(editable)) {
            binding.loginButton.setEnabled(true)
        } else {
            binding.loginButton.setEnabled(false)
        }
    }

    private fun authCodeLogin() {
        val intent = Intent(this, SMSLoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun register() {
        val intent = Intent(
            this@LoginActivity,
            SMSLoginActivity::class.java
        )
        val bundle = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.fade_in, R.anim.fade_out
        ).toBundle()
        startActivity(intent, bundle)
        finish()
    }

    private fun login() {
        val account: String = binding.phoneNumberEditText.text.toString().trim { it <= ' ' }
        val password: String = binding.passwordEditText.text.toString().trim { it <= ' ' }
        lifecycleScope.launch {
            val params = hashMapOf<String, Any>().apply {
                put("mobile", account)
                put("password", password)
                put("platform", 2)
                put("clientId", DeviceMgr.getClientId(this@LoginActivity))
            }
            val result = AppRepo.apiService.passwordLogin(params)
            if (result is ApiResult.Success && result.data.code == 0){
                val userId = result.data.data.userId
                val token = result.data.data.token
                KeyStoreUtil.saveData(this@LoginActivity, "wf_userId", userId)
                KeyStoreUtil.saveData(this@LoginActivity, "wf_token", token)
                //todo 需要连接IM Server
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this@LoginActivity, "出错啦", Toast.LENGTH_SHORT).show()
            }
        }
    }
}