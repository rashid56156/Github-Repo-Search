package com.github.search.view.ui

import androidx.lifecycle.ViewModel
import com.github.search.api.Constants
import com.github.search.api.GithubApiService
import com.github.search.models.RepoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers



class RepoListViewModel(private val api: GithubApiService, private val mView: RepoListView): ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    fun searchGithubRepositories(query: String, page: Int) {
        compositeDisposable.add(api.searchRepositories(query, Constants.PER_PAGE, page)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io()).subscribe({ response: RepoModel -> mView.didGetRepositories(response) }) { throwable: Throwable ->
                mView.errorProcessingRequest(throwable.message!!)
                throwable.printStackTrace()
            })
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}