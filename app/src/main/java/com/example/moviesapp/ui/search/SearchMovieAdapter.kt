package com.example.moviesapp.ui.search

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
import com.example.moviesapp.ui.ConstantsUi

class SearchMovieAdapter(
    private val onMovieClicked: (Int) -> Unit
): RecyclerView.Adapter<SearchMovieAdapter.SearchMovieViewHolder>() {

    private var movies: List<Movie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.search_movie_item, parent, false)
        return SearchMovieViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) {
        val movie: Movie = movies[position]
        holder.bind(movie, onMovieClicked)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    class SearchMovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var ivPoster: ImageView = itemView.findViewById(R.id.search_movie_item_poster)
        private var tvTitle: TextView = itemView.findViewById(R.id.search_movie_item_title)

        fun bind(movie: Movie, onMovieClicked: (Int) -> Unit) {
            // accessibility
            itemView.contentDescription = movie.originalTitle
            // rest of the views
            Glide.with(itemView.context)
                .load(ConstantsUi.IMAGE_URL + movie.posterPath)
                .into(ivPoster)
            tvTitle.text = movie.originalTitle
            itemView.setOnClickListener {
                movie.id?.let { onMovieClicked(it) }
            }
        }
    }
}