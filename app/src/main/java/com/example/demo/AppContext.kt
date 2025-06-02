package com.example.demo

import com.huaxia.xlib.UtilsApp
import com.tencent.mmkv.MMKV



class AppContext : UtilsApp() {
    override fun onCreate() {
        super.onCreate()
        val rootDir = MMKV.initialize(this)
    }
}