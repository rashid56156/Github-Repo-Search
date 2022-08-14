package com.github.search.di.module


import android.app.Application
import dagger.Provides
import dagger.Module
import javax.inject.Singleton

/**
 * Dagger AppModule
 */

@Module
class AppModule(application: Application) {
    private val mApplication: Application?

    @Provides
    @Singleton
    fun providesApplication(): Application? {
        return mApplication
    }

    init {
        mApplication = application
    }
}