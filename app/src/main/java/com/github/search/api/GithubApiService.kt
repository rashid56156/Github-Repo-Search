package com.github.search.api


import com.github.search.models.RepoModel
import io.reactivex.Flowable
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

/**
 * Api endpoints are defined inside of this interface using
 * retrofit annotations to encode details about the parameters and request method.
 */

interface GithubApiService {
    @GET("repositories")
   suspend fun searchRepositoriesAsync(
        @Query("q") query: String
    ): RepoModel

}