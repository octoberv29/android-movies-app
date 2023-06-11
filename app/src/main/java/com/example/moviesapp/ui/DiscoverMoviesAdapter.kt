package com.example.moviesapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.utils.Constants

class DiscoverMoviesAdapter(
    private val onMovieClicked: (Int) -> Unit
) : RecyclerView.Adapter<DiscoverMoviesAdapter.MovieViewHolder>() {

    private var movies = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie = movies[position]
        // TODO: fix !!
        holder.itemView.setOnClickListener { onMovieClicked(movie.id!!) }
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // TODO: add accessibility description
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)
        private val tvOriginalTitle: TextView = itemView.findViewById(R.id.tvOriginalTitle)
        private val tvVoteAverage: TextView = itemView.findViewById(R.id.tvVoteAverage)

        fun bind(movie: Movie) {
            // accessibility
            itemView.contentDescription = movie.originalTitle
            // views
            Glide.with(itemView.context)
                .load(Constants.IMAGE_URL + movie.posterPath)
                .into(ivThumbnail)
            tvOriginalTitle.text = movie.originalTitle
            tvVoteAverage.text = movie.voteAverage.toString()
        }
    }
}