package com.devid_academy.feedarticlescompose.data.network

import com.devid_academy.feedarticlescompose.data.manager.PreferencesManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preferencesManager.getToken()
        val originalRequest: Request = chain.request()
        val newRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .addHeader("token", token)
                .build()
        } else {
            originalRequest
        }
        return chain.proceed(newRequest)
    }
}