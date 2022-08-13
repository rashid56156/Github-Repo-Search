package com.github.search.view.ui

import com.github.search.models.RepoModel

/**
 * Interface to connect our viewmodel and view classes
 */

interface RepoListView {
    fun didFetchRepositories(response: RepoModel)
    fun errorFetchingRepositories(message: String)

}