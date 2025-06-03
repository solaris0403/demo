package com.huaxia.common.ext

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@SuppressLint("CommitTransaction")
fun AppCompatActivity.addFragmentToView(fragment: Fragment, containerId: Int) {
    supportFragmentManager.beginTransaction()
        .replace(containerId, fragment) // 先清除旧的 Fragment 并添加新的
        .commit()
}

@SuppressLint("CommitTransaction")
fun AppCompatActivity.safeAddFragment(fragment: Fragment, containerId: Int) {
    val transaction = supportFragmentManager.beginTransaction()
        .replace(containerId, fragment)
    // 如果已保存状态（可能是onStop之后），使用 commitAllowingStateLoss 防止崩溃
    if (!supportFragmentManager.isStateSaved) {
        transaction.commit()
    } else {
        transaction.commitAllowingStateLoss()
    }
}

inline fun LifecycleOwner.launch(context: CoroutineContext = Dispatchers.Main, crossinline block: suspend () -> Unit): Job {
    return lifecycleScope.launch(context) {
        block()
    }
}

inline fun LifecycleOwner.launchOnMain(crossinline block: suspend () -> Unit): Job {
    return lifecycleScope.launch(Dispatchers.Main) {
        block()
    }
}

inline fun LifecycleOwner.launchOnIO(crossinline block: suspend () -> Unit): Job {
    return lifecycleScope.launch(Dispatchers.IO) {
        block()
    }
}
