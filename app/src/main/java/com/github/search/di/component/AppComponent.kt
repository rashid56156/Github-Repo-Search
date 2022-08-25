package com.github.search.di.component

import com.github.search.di.module.AppModule
import com.github.search.di.module.NetworkModule
import com.github.search.di.module.ViewModelModule
import com.github.search.view.ui.RepoListFragment
import dagger.Component

import javax.inject.Singleton

/**
 * Dagger component interface. This interface is used by Dagger 2 to generate code
 * which uses the modules to fulfill the requested dependencies.
 * Here we will define the activities/fragments/classes where
 * we need to use DI to inject dependencies.
 */

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(fragment: RepoListFragment)
}