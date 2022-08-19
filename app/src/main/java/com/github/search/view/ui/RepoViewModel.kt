package com.github.search.view.ui

import androidx.lifecycle.*
import com.github.search.models.RepoModel
import com.github.search.models.RepoRepository
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * ViewModel class which is responsible for making the network call and then handing over the
 * response to the fragment.
 */
class RepoViewModel @Inject constructor(private val repository: RepoRepository): ViewModel(){

    val repoMediatorData = MediatorLiveData<RepoModel>()
    private var debouncePeriod: Long = 500
    private var searchJob: Job? = null
    private var _searchReposLiveData: LiveData<RepoModel>
    private var _topReposLiveData = MutableLiveData<RepoModel>()

    val repoLoadingStateLiveData = MutableLiveData<RepoLoadingState>()
    private val _searchFieldTextLiveData = MutableLiveData<String>()

    init {
        _searchReposLiveData = Transformations.switchMap(_searchFieldTextLiveData) {
            fetchRepoByQuery(it)
        }

        repoMediatorData.addSource(_topReposLiveData){
            repoMediatorData.value = it
        }

        repoMediatorData.addSource(_searchReposLiveData) {
            repoMediatorData.value = it
        }
    }

    fun onFragmentReady() {
        if (_searchReposLiveData.value?.items.isNullOrEmpty()) {
            fetchTopRepos()
        }
    }

    fun onSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(debouncePeriod)
                _searchFieldTextLiveData.value = query
        }
    }

    private fun fetchTopRepos() {
        repoLoadingStateLiveData.value = RepoLoadingState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = repository.fetchTopRepos()
                _topReposLiveData.postValue(movies)
                repoLoadingStateLiveData.postValue(RepoLoadingState.LOADED)
            } catch (e: Exception) {
                repoLoadingStateLiveData.postValue(RepoLoadingState.ERROR)
            }
        }
    }


    private fun fetchRepoByQuery(query: String): LiveData<RepoModel> {
        val liveData = MutableLiveData<RepoModel>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    repoLoadingStateLiveData.value = RepoLoadingState.LOADING
                }

                val response = repository.fetchRepoByQuery(query)
                liveData.postValue(response)

                repoLoadingStateLiveData.postValue(RepoLoadingState.LOADED)
            } catch (e: Exception) {
                repoLoadingStateLiveData.postValue(RepoLoadingState.ERROR)
                e.printStackTrace()
            }
        }
        return liveData
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }




}