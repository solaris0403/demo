package com.example.demo.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.demo.AppRepo
import com.example.demo.DeviceMgr
import com.example.demo.KeyStoreUtil
import com.example.demo.R
import com.example.demo.SimpleTextWatcher
import com.example.demo.component.BaseActivity
import com.example.demo.databinding.ActivitySmsloginBinding
import com.example.demo.network.ApiResult
import com.huaxia.xlib.sp.SpUtils
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.launch

class SMSLoginActivity : BaseActivity<ActivitySmsloginBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.passwordLoginTextView.setOnClickListener {
            authCodeLogin()
        }
        binding.loginButton.setOnClickListener {
            login()
        }
        binding.requestAuthCodeButton.setOnClickListener {
            requestAuthCode()
        }

        binding.phoneNumberEditText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                inputPhoneNumber(s)
            }
        })

        binding.authCodeEditText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                inputAuthCode(s)
            }
        })
    }

    fun inputPhoneNumber(editable: Editable) {
        val phone = editable.toString().trim { it <= ' ' }
        if (phone.length == 11 && countdownRunnable == null) {
            binding.requestAuthCodeButton.setEnabled(true)
        } else {
            binding.requestAuthCodeButton.setEnabled(false)
            binding.loginButton.setEnabled(false)
        }
    }

    fun inputAuthCode(editable: Editable) {
        if (editable.toString().length > 2) {
            binding.loginButton.setEnabled(true)
        }
    }

    private val handler = Handler()
    private var countdownSeconds = 60
    private var countdownRunnable: Runnable? = null

    private fun requestAuthCode() {
        val phoneNumber: String = binding.phoneNumberEditText.text.toString().trim { it <= ' ' }

        // Disable button immediately
        binding.requestAuthCodeButton.setEnabled(false)

        // Start countdown
        countdownSeconds = 60
        updateCountdownText()

        // Create countdown runnable if not exists
        if (countdownRunnable == null) {
            countdownRunnable = object : Runnable {
                override fun run() {
                    if (isFinishing) return

                    countdownSeconds--
                    updateCountdownText()

                    if (countdownSeconds > 0) {
                        // Continue countdown
                        handler.postDelayed(this, 1000)
                    } else {
                        // Reset button text and enable it
                        binding.requestAuthCodeButton.setText(getString(R.string.requesting_auth_code))
                        binding.requestAuthCodeButton.setEnabled(true)
                    }
                }
            }
        }

        // Start the countdown timer
        handler.postDelayed(countdownRunnable!!, 1000)

        // Request the auth code
        Toast.makeText(this, getString(R.string.requesting_auth_code), Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            val params = hashMapOf<String, Any>().apply {
                put("mobile", phoneNumber)
            }
            val result = AppRepo.apiService.requestAuthCode(params)
            if (result is ApiResult.Success){
                Toast.makeText(
                    this@SMSLoginActivity,
                    R.string.auth_code_request_success,
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    this@SMSLoginActivity,
                    getString(R.string.auth_code_request_failure),
                    Toast.LENGTH_SHORT
                ).show()
                // Reset countdown on failure
                resetCountdown()
            }
        }
    }

    private fun authCodeLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun login() {
        val phoneNumber: String = binding.phoneNumberEditText.getText().toString().trim { it <= ' ' }
        val authCode: String = binding.authCodeEditText.getText().toString().trim { it <= ' ' }

        binding.loginButton.setEnabled(false)

        //Platform_iOS = 1,
        //Platform_Android = 2,
        //Platform_Windows = 3,
        //Platform_OSX = 4,
        //Platform_WEB = 5,
        //Platform_WX = 6,
        //Platform_linux = 7,
        //Platform_iPad = 8,
        //Platform_APad = 9,

        //如果是android pad设备，需要改这里，另外需要在ClientService对象中修改设备类型，请在ClientService代码中搜索"android pad"
        //if（当前设备是android pad)
        //  params.put("platform", new Integer(9));
        //else
        lifecycleScope.launch {
            val params = hashMapOf<String, Any>().apply {
                put("mobile", phoneNumber)
                put("code", authCode)
            }
            val result = AppRepo.apiService.smsLogin(params)
            if (result is ApiResult.Success && result.data.code == 0){
                //连接IM
                val userId = result.data.data.userId
                val token = result.data.data.token
                val resetCode = result.data.data.resetCode
//                KeyStoreUtil.saveData(this@SMSLoginActivity, "wf_userId", userId)
//                KeyStoreUtil.saveData(this@SMSLoginActivity, "wf_token", token)
//                SpUtils.getInstance().put("wf_userId", userId)
//                SpUtils.getInstance().put("wf_token", token)
                MMKV.defaultMMKV().putString("wf_userId", userId)
                MMKV.defaultMMKV().putString("wf_token", token)

//                token = SpUtils.getInstance().getString("wf_token")
                //todo 需要连接IM Server

                val intent = Intent(this@SMSLoginActivity, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

//                val resetPasswordIntent = Intent(
//                    this@SMSLoginActivity,
//                    ResetPasswordActivity::class.java
//                )
//                resetPasswordIntent.putExtra("resetCode", resetCode)
//                startActivity(resetPasswordIntent)

                finish()
            }else{
                Toast.makeText(
                    this@SMSLoginActivity,
                    getString(R.string.sms_login_failure),
                    Toast.LENGTH_SHORT
                ).show()
                binding.loginButton.setEnabled(true)
            }
        }
    }

    private fun updateCountdownText() {
        if (countdownSeconds > 0) {
            binding.requestAuthCodeButton.setText(getString(R.string.retry_after_seconds, countdownSeconds))
        }
    }

    private fun resetCountdown() {
        // Remove callbacks
        if (countdownRunnable != null) {
            handler.removeCallbacks(countdownRunnable!!)
        }
        // Reset button
        binding.requestAuthCodeButton.setText(R.string.requesting_auth_code)
        binding.requestAuthCodeButton.setEnabled(true)
        countdownSeconds = 60
        countdownRunnable = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null && countdownRunnable != null) {
            handler.removeCallbacks(countdownRunnable!!)
        }
    }
}