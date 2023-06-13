package com.example.moviesapp.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.ui.ConstantsUi

/**
 * DiscoverMoviesAdapter is PagingDataAdapter used to show a list of movies on DiscoverMoviesFragment
 */
class DiscoverMoviesAdapter(
    private val onMovieClicked: (Int) -> Unit
) : PagingDataAdapter<Movie, DiscoverMoviesAdapter.MovieViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.discover_movie_item, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie: Movie? = getItem(position)
        movie?.let {
            holder.bind(movie)
            holder.itemView.setOnClickListener {
                movie.id?.let { movieId ->
                    onMovieClicked(movieId)
                }
            }
        }
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPoster: ImageView = itemView.findViewById(R.id.discover_movie_item_poster)
        private val tvOriginalTitle: TextView = itemView.findViewById(R.id.discover_movie_item_title)
        private val tvVoteAverage: TextView = itemView.findViewById(R.id.discover_movie_item_vote_average)

        fun bind(movie: Movie) {
            // accessibility
            itemView.contentDescription = movie.title
            // views
            Glide.with(itemView.context)
                .load(ConstantsUi.IMAGE_URL + movie.posterPath)
                .into(ivPoster)
            tvOriginalTitle.text = movie.title
            tvVoteAverage.text = movie.voteAverage.toString()
        }
    }
}