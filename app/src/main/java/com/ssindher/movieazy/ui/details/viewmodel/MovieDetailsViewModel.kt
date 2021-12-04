package com.ssindher.movieazy.ui.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssindher.movieazy.data.model.MovieDetails
import com.ssindher.movieazy.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails
    private val _movieDetailsError = MutableLiveData<String>()
    val movieDetailsError: LiveData<String> = _movieDetailsError

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val res = repository.getMovieDetails(movieId)
                _movieDetails.postValue(res)
            } catch (e: Exception) {
                _movieDetailsError.postValue(e.message)
            }
        }
    }
}