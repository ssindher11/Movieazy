package com.ssindher.movieazy.ui.details.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ssindher.movieazy.R
import com.ssindher.movieazy.data.model.MovieDetails
import com.ssindher.movieazy.data.model.MovieOverview
import com.ssindher.movieazy.ui.details.adapter.GenresAdapter
import com.ssindher.movieazy.ui.details.viewmodel.MovieDetailsViewModel
import com.ssindher.movieazy.utils.Utils
import kotlinx.android.synthetic.main.activity_movie_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.round

class MovieDetailsActivity : AppCompatActivity() {

    private val viewModel: MovieDetailsViewModel by viewModel()
    private var movie = MovieOverview.Result()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        window.sharedElementEnterTransition =
            TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition)
        ivMoviePoster.transitionName = "poster_transition"

        intent.getParcelableExtra<MovieOverview.Result>("movie")?.let {
            movie = it
            viewModel.fetchMovieDetails(it.id ?: 0)

            val posterUrl = "https://image.tmdb.org/t/p/w300${movie.poster_path}"
            Glide.with(this).load(posterUrl).placeholder(R.drawable.placeholder).into(ivMoviePoster)
            tvMovieName.text = movie.title

            val rating = (movie.vote_average ?: 0).toFloat() / 2
            rbStars.rating = rating
            tvMovieRating.text = "${round(rating * 100.0) / 100}"
            tvReviewCount.text = "Reviews: ${movie.vote_count}"

            tvSynopsis.text =
                if (movie.overview.isNullOrBlank()) getString(R.string.no_information_available) else movie.overview
        }

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        ibBack.setOnClickListener { super.onBackPressed() }
        ibShare.setOnClickListener {
            val data = "Hi! I am recommending you this movie: ${movie.title}. ${movie.overview}"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this movie")
            shareIntent.putExtra(Intent.EXTRA_TEXT, data)
            startActivity(Intent.createChooser(shareIntent, "Choose app"))
        }

        fabBook.setOnClickListener {
            Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        viewModel.movieDetails.observe(this) {
            setupGenreRV(it.genres ?: listOf())
            val metaData = arrayListOf<String>()

            val releases = it.release_dates?.results ?: listOf()
            if (releases.isNotEmpty()) {
                val usRelease = releases.find { r -> r.iso_3166_1 == "US" }
                usRelease?.release_dates?.let { dates ->
                    if (dates.isNotEmpty()) {
                        val certification =
                            dates.find { date -> !date.certification.isNullOrBlank() }
                        certification?.let { c -> metaData.add(c.certification ?: "") }
                    }
                }
            }

            val runTime = Utils.getHrMin(it.runtime ?: 0)
            if (runTime.isNotBlank()) metaData.add(runTime)

            val releaseDate = movie.release_date
            if (!releaseDate.isNullOrBlank()) metaData.add(Utils.getPrettyDate(releaseDate))

            tvMovieMeta.text = metaData.joinToString(" | ")
        }
        viewModel.movieDetailsError.observe(this) {
            tvSynopsis.text = getString(R.string.no_information_available)
        }
    }

    private fun setupGenreRV(list: List<MovieDetails.Genre>) {
        rvGenres.adapter = GenresAdapter(list)
    }
}