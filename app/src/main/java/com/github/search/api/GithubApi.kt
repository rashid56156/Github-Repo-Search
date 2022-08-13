package com.github.search.api


import com.github.search.models.Repo
import io.reactivex.Flowable
import retrofit2.http.*


interface GithubApi {
    @GET("repositories")
    fun searchRepositories(
        @Query("q") query: String = Constants.DEFAULT_QUERY,
        @Query("per_page") per_page: Int = Constants.PER_PAGE
    ): Flowable<Repo>

}