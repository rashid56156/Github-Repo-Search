package com.github.search.api

import android.os.SystemClock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * okhttp interceptor class to intercept network calls and make our required changes
 */
class GithubApiInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("User-Agent", "ANDROID 1.0.0)")
            .build()
        /**
         * putting a delay of 3 sec between two calls to manage the API rate limit
         * It is not an ideal solution but it works for such demo/test apps
         */
        SystemClock.sleep(2000)

        return chain.proceed(request)
    }
}