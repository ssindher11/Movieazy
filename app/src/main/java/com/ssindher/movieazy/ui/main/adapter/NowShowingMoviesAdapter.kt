package com.ssindher.movieazy.ui.main.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssindher.movieazy.R
import com.ssindher.movieazy.data.model.MovieOverview
import com.ssindher.movieazy.ui.details.activity.MovieDetailsActivity
import kotlinx.android.synthetic.main.item_now_showing_movie.view.*

class NowShowingMoviesAdapter(
    private val activityCtx: Context,
    private val list: MutableList<MovieOverview.Result>,
) : RecyclerView.Adapter<NowShowingMoviesAdapter.MovieVH>() {

    inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(movie: MovieOverview.Result, pos: Int) {
            itemView.apply {
                val posterUrl = "https://image.tmdb.org/t/p/w300${movie.poster_path}"
                Glide.with(this).load(posterUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(ivMoviePoster)

                setOnClickListener {
                    val intent = Intent(context, MovieDetailsActivity::class.java)
                    intent.putExtra("movie", movie)

                    ivMoviePoster.transitionName = "poster_transition"
                    val tPair = androidx.core.util.Pair<View, String>(
                        ivMoviePoster,
                        ivMoviePoster.transitionName
                    )

                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activityCtx as Activity,
                        tPair
                    )
                    activityCtx.startActivity(intent, options.toBundle())
                }

                setOnLongClickListener {
                    Toast.makeText(activityCtx, movie.title, Toast.LENGTH_SHORT).show()
                    true
                }

                ViewCompat.setTransitionName(ivMoviePoster, "${movie.title}_${movie.id}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH = MovieVH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_now_showing_movie, parent, false)
    )

    override fun onBindViewHolder(holder: MovieVH, position: Int) =
        holder.bindData(list[position], position)

    override fun getItemCount(): Int = list.size

    fun loadMoreItems(newList: MutableList<MovieOverview.Result>) {
        if (newList.isNotEmpty()) {
            list.addAll(newList)
            notifyItemInserted(list.lastIndex)
        }
    }
}