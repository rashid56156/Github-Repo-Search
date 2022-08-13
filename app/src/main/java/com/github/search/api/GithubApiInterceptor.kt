package com.github.search.api

import android.os.SystemClock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class GithubApiInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("User-Agent", "ANDROID 1.0.0)")
            .build()
        SystemClock.sleep(2000)
        return chain.proceed(request)
    }
}