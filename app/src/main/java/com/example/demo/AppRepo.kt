package com.example.demo

import com.example.demo.network.RetrofitClient

object AppRepo {
    private const val BASE_URL = "http://192.168.1.3:8080"

    val apiService by lazy {
        RetrofitClient.createService(BASE_URL, ApiService::class.java)
    }
}