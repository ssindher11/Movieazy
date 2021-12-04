package com.ssindher.movieazy.data.repository

import com.ssindher.movieazy.data.api.ApiInterface

class MovieRepository(private val apiInterface: ApiInterface) {

    suspend fun getPopularMovies() = apiInterface.getPopularMovies()

    suspend fun getNowShowingMovies(
        startDate: String,
        endDate: String,
        page: Int = 1
    ) = apiInterface.getNowShowingMovies(startDate, endDate, page)

    suspend fun getMovieDetails(movieId: Int) = apiInterface.getMovieDetails(movieId)
}