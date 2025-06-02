package com.example.demo.ext

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

@SuppressLint("CommitTransaction")
fun AppCompatActivity.addFragmentToView(fragment: Fragment, containerId: Int) {
    supportFragmentManager.beginTransaction()
        .replace(containerId, fragment) // 先清除旧的 Fragment 并添加新的
        .commit()
}