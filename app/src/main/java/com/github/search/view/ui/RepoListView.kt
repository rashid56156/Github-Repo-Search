package com.github.search.view.ui

import com.github.search.models.RepoModel

interface RepoListView {

    fun didGetRepositories(response: RepoModel)
    fun errorProcessingRequest(message: String)

}