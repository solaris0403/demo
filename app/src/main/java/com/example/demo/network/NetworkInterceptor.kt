package com.example.demo.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 拦截器集合
 */
object NetworkInterceptor {

    // 日志拦截器
    object LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            logRequest(request)

            val response = chain.proceed(request)
            return logResponse(response)
        }

        private fun logRequest(request: Request) {
            println("""
                ⇢ URL: ${request.url}
                ⇢ Method: ${request.method}
                ⇢ Headers: ${request.headers}
            """.trimIndent())
        }

        private fun logResponse(response: Response): Response {
            println("""
                ⇠ Code: ${response.code}
                ⇠ Headers: ${response.headers}
                ⇠ Body: ${response.peekBody(1024).string()}
            """.trimIndent())
            return response
        }
    }

    // 认证拦截器
    class AuthInterceptor(private val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)
        }
    }

    // 重试拦截器
    class RetryInterceptor(private val maxRetries: Int = 3) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var retryCount = 0
            var response: Response

            do {
                response = chain.proceed(chain.request())
                if (response.isSuccessful) break
                retryCount++
            } while (retryCount < maxRetries)

            return response
        }
    }
}