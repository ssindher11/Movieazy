package com.ssindher.movieazy.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssindher.movieazy.R
import com.ssindher.movieazy.data.model.MovieOverview
import com.ssindher.movieazy.utils.DualEventListeners
import kotlinx.android.synthetic.main.item_now_showing_movie.view.*

class NowShowingMoviesAdapter(
    private val list: MutableList<MovieOverview.Result>,
    private val dualEventListeners: DualEventListeners
) : RecyclerView.Adapter<NowShowingMoviesAdapter.MovieVH>() {

    inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(movie: MovieOverview.Result, pos: Int) {
            itemView.apply {
                val posterUrl = "https://image.tmdb.org/t/p/w300${movie.poster_path}"
                Glide.with(this).load(posterUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(ivMoviePoster)

                setOnClickListener {
                    dualEventListeners.click(pos, 1)
                }

                setOnLongClickListener {
                    dualEventListeners.click(pos, 2)
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