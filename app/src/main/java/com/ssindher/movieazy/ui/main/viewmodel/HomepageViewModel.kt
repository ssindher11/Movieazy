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

    /*private val _popularMoviesList = MutableLiveData<List<MovieOverview.Result>>()
    val popularMoviesList: LiveData<List<MovieOverview.Result>> = _popularMoviesList
    private lateinit var popularMoviesListDisposable: DisposableSingleObserver<MovieOverview>
    private val _popularMoviesListLoader = MutableLiveData<Boolean>()
    val popularMoviesListLoader: LiveData<Boolean> = _popularMoviesListLoader
    private val _popularMoviesListError = MutableLiveData<String>()
    val popularMoviesListError: LiveData<String> = _popularMoviesListError

    fun getPopularMovies() {
        popularMoviesListDisposable = object : DisposableSingleObserver<MovieOverview>() {
            override fun onSuccess(t: MovieOverview) {
                _popularMoviesList.postValue(t.results ?: listOf())
                _popularMoviesListLoader.postValue(false)
            }

            override fun onError(e: Throwable) {
                _popularMoviesListLoader.postValue(false)
                _popularMoviesListError.postValue(Utils.getErrorMessage(e))
            }
        }
        _popularMoviesListLoader.postValue(true)
        repository.getPopularMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(popularMoviesListDisposable)
        compositeDisposable.add(popularMoviesListDisposable)
    }


    private val _nowShowingMoviesList = MutableLiveData<List<MovieOverview.Result>>()
    val nowShowingMoviesList: LiveData<List<MovieOverview.Result>> = _nowShowingMoviesList
    private lateinit var nowShowingMoviesListDisposable: DisposableSingleObserver<MovieOverview>
    private val _nowShowingMoviesListLoader = MutableLiveData<Boolean>()
    val nowShowingMoviesListLoader: LiveData<Boolean> = _nowShowingMoviesListLoader
    private val _nowShowingMoviesListError = MutableLiveData<String>()
    val nowShowingMoviesListError: LiveData<String> = _nowShowingMoviesListError

    fun getNowShowingMovies(page: Int = 1) {
        nowShowingMoviesListDisposable = object : DisposableSingleObserver<MovieOverview>() {
            override fun onSuccess(t: MovieOverview) {
                _nowShowingMoviesList.postValue(t.results ?: listOf())
                _nowShowingMoviesListLoader.postValue(false)
            }

            override fun onError(e: Throwable) {
                _nowShowingMoviesListLoader.postValue(false)
                _nowShowingMoviesListError.postValue(Utils.getErrorMessage(e))
            }
        }
        _nowShowingMoviesListLoader.postValue(true)
        val cal = Calendar.getInstance()
        val endDate = Utils.getFormattedDate(cal.time)
        cal.add(Calendar.MONTH, -1)
        val startDate = Utils.getFormattedDate(cal.time)
        repository.getNowShowingMovies(startDate, endDate, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(nowShowingMoviesListDisposable)
        compositeDisposable.add(nowShowingMoviesListDisposable)
    }*/

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


    /*fun getMovieDetails(movieId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getMovieDetails(movieId)))
        } catch (e: Exception) {
            emit(Resource.error(null, Utils.getErrorMessage(e)))
        }
    }*/
}