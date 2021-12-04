package com.ssindher.movieazy.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.ssindher.movieazy.R
import com.ssindher.movieazy.data.model.MovieOverview
import com.ssindher.movieazy.ui.details.activity.MovieDetailsActivity
import com.ssindher.movieazy.ui.main.adapter.NowShowingMoviesAdapter
import com.ssindher.movieazy.ui.main.adapter.PopularMoviesAdapter
import com.ssindher.movieazy.ui.main.viewmodel.HomepageViewModel
import com.ssindher.movieazy.utils.DualEventListeners
import com.ssindher.movieazy.utils.EventListeners
import kotlinx.android.synthetic.main.activity_homepage.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class HomepageActivity : AppCompatActivity() {

    private val viewModel: HomepageViewModel by viewModel()
    private var isNowShowingLoading = false
    private val sliderHandler = Handler()
    private val sliderRunnable =
        Runnable { vpPopularMovies.setCurrentItem(vpPopularMovies.currentItem + 1, true) }

    private lateinit var nowShowingMoviesAdapter: NowShowingMoviesAdapter
    private var nowShowingMoviesList: MutableList<MovieOverview.Result> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        setupUI()
        setupObservers()

        viewModel.fetchPopularMovies()
        viewModel.fetchNowShowingMovies()
    }

    private fun setupUI() {
        vpPopularMovies.clipToPadding = false
        vpPopularMovies.clipChildren = false
        vpPopularMovies.offscreenPageLimit = 3
        vpPopularMovies.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r: Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        vpPopularMovies.setPageTransformer(compositePageTransformer)
        vpPopularMovies.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 5000)
            }
        })

        rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!(isNowShowingLoading && viewModel.isNowShowingFinished())) {
                        isNowShowingLoading = true
                        viewModel.loadNextNowShowingPage()
                    }
                }
            }
        })
    }

    private fun setupObservers() {
        viewModel.nowShowingMoviesList.observe(this) {
            if (viewModel.getCurrentPageIndex() == 1) {
                nowShowingMoviesList = it.toMutableList()
                setupNowShowingRV()
            } else {
                if (isNowShowingLoading) {
                    nowShowingMoviesList.addAll(it)
                    nowShowingMoviesAdapter.loadMoreItems(it.toMutableList())
                    isNowShowingLoading = false
                }
            }
        }
        viewModel.nowShowingMoviesListError.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.popularMoviesList.observe(this) { setupPopularVP(it ?: listOf()) }
        viewModel.popularMoviesListError.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupNowShowingRV() {
        rvMovies.layoutManager = GridLayoutManager(this, 3)
        nowShowingMoviesAdapter =
            NowShowingMoviesAdapter(
                nowShowingMoviesList,
                object : DualEventListeners {
                    override fun click(pos: Int, flag: Int) {
                        val movie = nowShowingMoviesList[pos]
                        when (flag) {
                            1 -> {
                                val intent =
                                    Intent(this@HomepageActivity, MovieDetailsActivity::class.java)
                                intent.putExtra("movie", movie)
                                startActivity(intent)
                            }
                            2 -> Toast.makeText(
                                this@HomepageActivity,
                                movie.title,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        rvMovies.adapter = nowShowingMoviesAdapter
    }

    private fun setupPopularVP(list: List<MovieOverview.Result>) {
        vpPopularMovies.adapter =
            PopularMoviesAdapter(list.toMutableList(), vpPopularMovies, object : EventListeners {
                override fun click(pos: Int) {
                    val intent = Intent(this@HomepageActivity, MovieDetailsActivity::class.java)
                    intent.putExtra("movie", list[pos % list.size])
                    startActivity(intent)
                }
            })
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 5000)
    }
}