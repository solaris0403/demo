package com.example.demo.network

/**
 * 统一封装的 API 结果类型
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class ApiError(
        val errorCode: Int?,
        val message: String?,
        val debug: String?,
        val httpCode: Int,
        val rawResponse: String?
    ) : ApiResult<Nothing>()

    data class NetworkError(val throwable: Throwable) : ApiResult<Nothing>()
    data class UnKnowError(val throwable: Throwable?) : ApiResult<Nothing>()
}