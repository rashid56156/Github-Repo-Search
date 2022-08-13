package com.github.search.view.ui

import androidx.lifecycle.*
import com.github.search.api.Constants
import com.github.search.api.GithubApiService
import com.github.search.models.ErrorResponse
import com.github.search.models.RepoModel
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Viewmodel class which is responsible for making the network call and then handing over the
 * response to the fragment.
 */
class RepoListViewModel(private val api: GithubApiService, private val mView: RepoListView): ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    fun searchGithubRepositories(query: String, page: Int) {
        compositeDisposable.add(api.searchRepositories(query, Constants.PER_PAGE, page)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io()).subscribe({ response: RepoModel -> mView.didFetchRepositories(response) }) { throwable: Throwable ->
                if (throwable is HttpException) {
                    try {
                        val errorResponse = Gson().fromJson(throwable.response().errorBody()!!.string(), ErrorResponse::class.java)
                        mView.errorFetchingRepositories(errorResponse.message.plus(". ").plus(errorResponse.errors?.get(0)?.message ?: ""))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        mView.errorFetchingRepositories(throwable.message!!)
                    }
                }
                throwable.printStackTrace()
            })

    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}