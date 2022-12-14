package com.github.search.models

import com.github.search.api.Constants
import com.github.search.api.GithubApiService
import javax.inject.Inject

class RepoRepository @Inject constructor(private val apiService: GithubApiService) {
  suspend fun fetchRepoByQuery(queryText: String): RepoModel {
    return apiService.searchRepositoriesAsync(queryText)
  }

  suspend fun fetchTopRepos(): RepoModel {
    return apiService.searchRepositoriesAsync("q=stars:5")
  }
}