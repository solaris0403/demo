package com.example.demo.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

// ========== 自定义 CallAdapter，实现 ApiResult 适配 ==========
class ApiResultAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType)) return null
        check(returnType is ParameterizedType) { "Call return type must be parameterized as Call<ApiResult<Foo>>" }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != ApiResult::class.java) return null

        check(responseType is ParameterizedType) { "ApiResult must be parameterized as ApiResult<Foo>" }
        val successBodyType = getParameterUpperBound(0, responseType)

        return ApiResultAdapter<Any>(successBodyType)
    }
}

