package com.huaxia.common

import android.annotation.SuppressLint
import android.content.Context
import com.tencent.mmkv.MMKV

/**
 *     @Override
 *     protected void attachBaseContext(Context base) {
 *         super.attachBaseContext(base);
 *         MultiDex.install(base);
 *     }
 *     由主工程判断要不要开启MultiDex，并且在attachBaseContext中设置
 */
@SuppressLint("StaticFieldLeak")
object AppContext {
    private lateinit var context: Context

    public fun init(context: Context) {
        this.context = context.applicationContext
        init()
    }

    fun get(): Context {
        if (!::context.isInitialized) {
            throw IllegalStateException("AppContext is not initialized. Call AppContext.init() in Application.onCreate()")
        }
        return context
    }

    /**
     *
     */
    private fun init(){
        MMKV.initialize(context)
    }
}