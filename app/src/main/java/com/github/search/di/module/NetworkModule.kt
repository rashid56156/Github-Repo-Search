package com.github.search.di.module

import android.app.Application
import com.github.search.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import com.github.search.api.Constants
import com.github.search.api.GithubApi
import com.github.search.api.GithubApiInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls()
                //.registerTypeAdapterFactory(AutoValueGsonFactory.Companion.create())
                .create()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson?): GsonConverterFactory {
        return GsonConverterFactory.create(gson!!)
    }


    @Provides
    @Singleton
    fun provideCache(application: Application): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(application.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }

    @Provides
    @Singleton
    fun provideContentApi(factory: GsonConverterFactory?): GithubApi {
        return Retrofit.Builder().baseUrl(Constants.GITHUB_API)
            .client(getOkHttpClient(GithubApiInterceptor()))
            .addConverterFactory(factory!!)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }


    private fun getOkHttpClient(interceptor: Interceptor?): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return if (interceptor == null) {
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()
        }
    }
}