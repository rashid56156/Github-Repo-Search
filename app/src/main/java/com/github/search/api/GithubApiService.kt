package com.github.search.api


import com.github.search.models.RepoModel
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Api endpoints are defined inside of this interface using
 * retrofit annotations to encode details about the parameters and request method.
 */

interface GithubApiService {
    @GET("repositories")
    fun searchRepositories(
        @Query("q") query: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ): Flowable<RepoModel>

}