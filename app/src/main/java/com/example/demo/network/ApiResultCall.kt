package com.example.demo.network

import okhttp3.Request
import okio.Timeout
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

// 拦截 Retrofit 请求的响应，统一转换为 ApiResult<T> 进行错误处理。
/**
 * @param delegate 原始 Retrofit Call 对象
 */
class ApiResultCall<T : Any>(private val delegate: Call<T>) : Call<ApiResult<T>> {
    override fun enqueue(callback: Callback<ApiResult<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                val httpCode = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        //成功请求，包装成 ApiResult.Success
                        callback.onResponse(
                            this@ApiResultCall,
                            Response.success(ApiResult.Success(body))
                        )
                    } else {
                        //返回体为空，返回未知错误
                        handleError(ApiResult.UnKnowError(null), callback)
                    }
                } else {
                    if (error != null) {
                        val errorBody = error.string()
                        try {
                            val json = JSONObject(errorBody)
                            val apiError = ApiResult.ApiError(
                                json.optInt("code"),
                                json.optString("message"),
                                json.optString("debug"),
                                httpCode,
                                errorBody
                            )
                            handleError(apiError, callback)
                        } catch (e: Exception) {
                            handleError(ApiResult.UnKnowError(e), callback)
                        }
                    } else {
                        //未知错误
                        handleError(ApiResult.ApiError(0, null, null, httpCode, null), callback)
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                //处理请求失败（网络错误、超时等）
                val error =
                    if (t is IOException) ApiResult.NetworkError(t) else ApiResult.UnKnowError(t)
                handleError(error, callback)
            }
        })
    }

    private fun handleError(error: ApiResult<T>, callback: Callback<ApiResult<T>>) {
//        errorHandler?.invoke(error)
        callback.onResponse(this, Response.success(error))
    }

    override fun isExecuted() = delegate.isExecuted
    override fun clone() = ApiResultCall(delegate.clone())
    override fun isCanceled() = delegate.isCanceled
    override fun cancel() = delegate.cancel()
    override fun execute(): Response<ApiResult<T>> {
        throw UnsupportedOperationException("ApiResultCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}