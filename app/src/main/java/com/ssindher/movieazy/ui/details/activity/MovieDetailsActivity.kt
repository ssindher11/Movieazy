package com.ssindher.movieazy.ui.details.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ssindher.movieazy.R
import com.ssindher.movieazy.data.model.MovieOverview
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlin.math.round

class MovieDetailsActivity : AppCompatActivity() {

    private var movie = MovieOverview.Result()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        intent.getParcelableExtra<MovieOverview.Result>("movie")?.let {
            movie = it
            val posterUrl = "https://image.tmdb.org/t/p/w300${movie.poster_path}"
            Glide.with(this).load(posterUrl).into(ivMoviePoster)
            tvMovieName.text = movie.title
            tvMovieMeta.text = movie.release_date

            val rating = (movie.vote_average ?: 0).toFloat() / 2
            rbStars.rating = rating
            tvMovieRating.text = "${round(rating * 100.0) / 100}"
            tvReviewCount.text = "Reviews: ${movie.vote_count}"

            tvSynopsis.text = movie.overview
        }

        setupUI()
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
}