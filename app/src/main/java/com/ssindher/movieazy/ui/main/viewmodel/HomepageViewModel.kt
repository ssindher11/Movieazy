package com.ssindher.movieazy.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssindher.movieazy.data.model.MovieOverview
import com.ssindher.movieazy.data.repository.MovieRepository
import com.ssindher.movieazy.utils.Utils
import kotlinx.coroutines.launch
import java.util.*

class HomepageViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _popularMoviesList = MutableLiveData<List<MovieOverview.Result>>()
    val popularMoviesList: LiveData<List<MovieOverview.Result>> = _popularMoviesList
    private val _popularMoviesListError = MutableLiveData<String>()
    val popularMoviesListError: LiveData<String> = _popularMoviesListError

    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                val res = repository.getPopularMovies()
                _popularMoviesList.postValue(res.results ?: listOf())
            } catch (e: Exception) {
                _popularMoviesListError.postValue(Utils.getErrorMessage(e))
            }
        }
    }


    private var _currentPageIndex: Int = 1
    private var totalPages: Int = -1
    fun getCurrentPageIndex() = _currentPageIndex
    fun loadNextNowShowingPage() {
        _currentPageIndex += 1
        fetchNowShowingMovies()
    }
    fun isNowShowingFinished() = _currentPageIndex >= totalPages

    private val _nowShowingMoviesList = MutableLiveData<List<MovieOverview.Result>>()
    val nowShowingMoviesList: LiveData<List<MovieOverview.Result>> = _nowShowingMoviesList
    private val _nowShowingMoviesListError = MutableLiveData<String>()
    val nowShowingMoviesListError: LiveData<String> = _nowShowingMoviesListError

    fun fetchNowShowingMovies() {
        viewModelScope.launch {
            try {
                val cal = Calendar.getInstance()
                val endDate = Utils.getFormattedDate(cal.time)
                cal.add(Calendar.MONTH, -1)
                val startDate = Utils.getFormattedDate(cal.time)
                val res = repository.getNowShowingMovies(
                    startDate,
                    endDate,
                    _currentPageIndex
                )
                totalPages = res.total_pages ?: -1
                _nowShowingMoviesList.postValue(res.results ?: listOf())
            } catch (e: Exception) {
                _nowShowingMoviesListError.postValue(Utils.getErrorMessage(e))
            }
        }
    }
}