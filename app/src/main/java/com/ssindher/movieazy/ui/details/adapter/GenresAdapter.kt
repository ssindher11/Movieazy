package com.ssindher.movieazy.ui.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssindher.movieazy.R
import com.ssindher.movieazy.data.model.MovieDetails
import kotlinx.android.synthetic.main.item_genre.view.*

class GenresAdapter(private val list: List<MovieDetails.Genre>) :
    RecyclerView.Adapter<GenresAdapter.GenreVH>() {

    inner class GenreVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(genre: MovieDetails.Genre) {
            itemView.tvGenre.text = genre.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreVH = GenreVH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
    )

    override fun onBindViewHolder(holder: GenreVH, position: Int) = holder.bindData(list[position])

    override fun getItemCount(): Int = list.size
}