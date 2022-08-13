package com.github.search.view.ui

import androidx.lifecycle.ViewModel
import com.github.search.api.GithubApi
import com.github.search.models.Repo
import com.github.search.models.RepoItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers



class RepoListViewModel(private val api: GithubApi, private val mView: RepoListView): ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    fun getWeatherForecast() {
        compositeDisposable.add(api.searchRepositories()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io()).subscribe({ response: Repo -> mView.didGetRepositories(response) }) { throwable: Throwable ->
                mView.errorProcessingRequest(throwable.message!!)
                throwable.printStackTrace()
            })
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}