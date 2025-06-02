package com.example.demo.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.demo.AppRepo
import com.example.demo.R
import com.example.demo.component.BaseActivity
import com.example.demo.databinding.ActivityResetPasswordBinding
import com.example.demo.network.ApiResult
import kotlinx.coroutines.launch

class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding>() {

    private var resetCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetCode = intent.getStringExtra("resetCode")
        if (!TextUtils.isEmpty(resetCode)) {
            binding.authCodeFrameLayout.setVisibility(View.GONE)
        }
        binding.confirmButton.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword(){
        val newPassword: String = binding.newPasswordEditText.getText().toString().trim { it <= ' ' }
        val confirmPassword: String =
            binding.confirmPasswordEditText.getText().toString().trim { it <= ' ' }
        if (!TextUtils.equals(newPassword, confirmPassword)) {
            Toast.makeText(this, R.string.password_not_match, Toast.LENGTH_SHORT).show()
            return
        }
        val code =
            if (TextUtils.isEmpty(resetCode)) binding.authCodeEditText.getText().toString() else resetCode!!

        lifecycleScope.launch {
            val params = hashMapOf<String, Any>().apply {
                put("resetCode", code)
                put("newPassword", newPassword)
            }
            val result = AppRepo.apiService.resetPassword(params)
            if (result is ApiResult.Success){
                Toast.makeText(
                    this@ResetPasswordActivity,
                    R.string.reset_password_success,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }else{
                Toast.makeText(
                    this@ResetPasswordActivity,
                    getString(R.string.reset_password_failure),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}