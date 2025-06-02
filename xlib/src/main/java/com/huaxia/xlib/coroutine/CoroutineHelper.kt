package com.huaxia.xlib.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

/**
 * 协程工具类，用于简化协程的管理和线程调度。
 */
object CoroutineHelper {

    private val globalScope: CoroutineScope by lazy((LazyThreadSafetyMode.SYNCHRONIZED)) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    /**
     * 在主线程执行任务
     * @param block 要执行的任务
     */
    fun runOnMain(block: suspend CoroutineScope.() -> Unit): Job {
        return globalScope.launch(Dispatchers.Main) {
            block()
        }
    }

    /**
     * 在IO线程执行任务
     * @param block 要执行的任务
     */
    fun runOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return globalScope.launch(Dispatchers.IO) {
            block()
        }
    }

    /**
     * 取消所有协程任务
     */
    fun cancelAll() {
        globalScope.coroutineContext.cancelChildren()
    }
}