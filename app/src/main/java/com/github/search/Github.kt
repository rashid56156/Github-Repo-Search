package com.github.search

import android.app.Application
import com.github.search.di.component.AppComponent
import com.github.search.di.component.DaggerAppComponent
import com.github.search.di.module.AppModule
import com.github.search.di.module.NetworkModule


class GithubApplication : Application() {

    init{
        instance_ = this
        component = createDaggerComponent()

    }

    override fun onCreate() {
        super.onCreate()

    }

    companion object {
        private var component: AppComponent? = null
        private lateinit var instance_: GithubApplication

        fun getInstance() = instance_


        fun getComponent(): AppComponent {
            return (if (component == null) createDaggerComponent() else component!!)
        }


        fun clearComponent() {
            component = null
        }

        fun createDaggerComponent(): AppComponent {
            return DaggerAppComponent.builder().appModule(AppModule(getInstance())).networkModule(
                NetworkModule()
            ).build()
        }



    }
}