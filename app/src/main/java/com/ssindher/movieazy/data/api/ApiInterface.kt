package com.ssindher.movieazy.data.api

import com.ssindher.movieazy.data.model.MovieDetails
import com.ssindher.movieazy.data.model.MovieOverview
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "429b19fc38dce8f4b2e12068b0033908"
    ): MovieOverview

    @GET("discover/movie")
    suspend fun getNowShowingMovies(
        @Query("primary_release_date.gte") startDate: String,
        @Query("primary_release_date.lte") endDate: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = "429b19fc38dce8f4b2e12068b0033908"
    ): MovieOverview

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String = "429b19fc38dce8f4b2e12068b0033908"
    ): MovieDetails
}