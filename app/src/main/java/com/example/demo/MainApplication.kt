package com.example.demo

import android.app.Application
import android.content.Context
import com.huaxia.common.AppContext

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.init(this)
    }
}