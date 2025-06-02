package com.example.demo

import com.example.demo.network.ApiResult
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/open/login_pwd")
    suspend fun passwordLogin(@Body body: Map<String, @JvmSuppressWildcards Any>): ApiResult<LoginData>

    @POST("/api/open/send_code")
    suspend fun requestAuthCode(@Body body: Map<String, @JvmSuppressWildcards Any>): ApiResult<String>

    @POST("/api/open/login")
    suspend fun smsLogin(@Body body: Map<String, @JvmSuppressWildcards Any>): ApiResult<LoginData>

    /**
     * params.put("mobile", phoneNumber);
     */
    @POST("/send_reset_code")
    suspend fun requestResetAuthCode(@Body body: Map<String, @JvmSuppressWildcards Any>): ApiResult<String>


    /**
     *  params.put("resetCode", code);
     *  params.put("newPassword", password);
     */
    @POST("/reset_pwd")
    suspend fun resetPassword(@Body body: Map<String, @JvmSuppressWildcards Any>): ApiResult<String>
}