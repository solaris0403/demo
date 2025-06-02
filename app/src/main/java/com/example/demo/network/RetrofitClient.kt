package com.example.demo.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * 客户端管理
 */
object RetrofitClient {
    // 使用线程安全的 ConcurrentHashMap
    private val clients = ConcurrentHashMap<String, Retrofit>()

    /**
     * 创建 Retrofit Service
     * @param baseUrl: 请求的 BaseUrl
     * @param serviceClass: Retrofit API 接口
     * @param config: 可选的 OkHttp 配置
     */
    fun <T> createService(
        baseUrl: String,
        serviceClass: Class<T>,
        config: OkHttpClient.Builder.() -> Unit = {}
    ): T {
        return getRetrofit(baseUrl, config).create(serviceClass)
    }

    /**
     * 获取 Retrofit 实例（支持多 BaseUrl）
     */
    private fun getRetrofit(baseUrl: String, config: OkHttpClient.Builder.() -> Unit): Retrofit {
        return clients[baseUrl] ?: synchronized(this) {  // 保证线程安全
            clients[baseUrl] ?: buildRetrofit(baseUrl, config).also { clients[baseUrl] = it }
        }
    }

    /**
     * 构建 Retrofit
     */
    private fun buildRetrofit(baseUrl: String, config: OkHttpClient.Builder.() -> Unit): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(buildOkHttpClient(config))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())  // JSON 解析
            .addCallAdapterFactory(ApiResultAdapterFactory())
            .build()
    }

    /**
     * 构建 OkHttpClient，支持外部自定义拦截器
     */
    private fun buildOkHttpClient(config: OkHttpClient.Builder.() -> Unit): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(provideLoggingInterceptor())  // 默认日志拦截器
            .apply(config)  // 外部自定义配置
            .build()
    }

    /**
     * 提供默认的日志拦截器
     */
    private fun provideLoggingInterceptor(): okhttp3.logging.HttpLoggingInterceptor {
        return okhttp3.logging.HttpLoggingInterceptor().apply {
            level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        }
    }
}