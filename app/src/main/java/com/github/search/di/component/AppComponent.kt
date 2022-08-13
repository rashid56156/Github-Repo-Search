package com.github.search.di.component

import com.github.search.di.module.AppModule
import com.github.search.di.module.NetworkModule
import com.github.search.view.ui.RepoListFragment
import dagger.Component

import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    fun inject(fragment: RepoListFragment)


}