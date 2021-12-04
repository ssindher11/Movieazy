package com.ssindher.movieazy.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ssindher.movieazy.R
import com.ssindher.movieazy.data.model.MovieOverview
import com.ssindher.movieazy.utils.DualEventListeners
import kotlinx.android.synthetic.main.item_popular_movie.view.*

class PopularMoviesAdapter(
    private val list: MutableList<MovieOverview.Result>,
    private val viewPager: ViewPager2,
    private val dualEventListeners: DualEventListeners
) : RecyclerView.Adapter<PopularMoviesAdapter.PopularVH>() {

    inner class PopularVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(movie: MovieOverview.Result, pos: Int) {
            itemView.apply {
                val backdropUrl = "https://image.tmdb.org/t/p/w300${movie.backdrop_path}"
                Glide.with(this).load(backdropUrl).into(ivMovieBackdrop)

                setOnClickListener { dualEventListeners.click(pos, 1) }
                setOnLongClickListener {
                    dualEventListeners.click(pos, 2)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularVH = PopularVH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_popular_movie, parent, false)
    )

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: PopularVH, position: Int) {
        holder.bindData(list[position], position)
        if (position == list.size - 2) {
            viewPager.post {
                list.addAll(list)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = list.size
}