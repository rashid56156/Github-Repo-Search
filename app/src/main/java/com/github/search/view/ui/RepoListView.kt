package com.github.search.view.ui

import com.github.search.models.Repo

interface RepoListView {

    fun didGetRepositories(response: Repo)
    fun errorProcessingRequest(message: String)

}