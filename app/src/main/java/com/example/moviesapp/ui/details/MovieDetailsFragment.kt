package com.example.moviesapp.ui.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.ui.MainActivity
import com.example.moviesapp.ui.ConstantsUi
import com.example.moviesapp.ui.ConstantsUi.Companion.MOVIE_ID_KEY
import dagger.hilt.android.AndroidEntryPoint

/**
 * MovieDetailsFragment is a fragment that shows details for a given movie using movieId
 */
@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var ivPoster: ImageView
    private lateinit var tvOriginalTitle: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvVoteAverage: TextView
    private lateinit var tvOverview: TextView
    private lateinit var tvLanguage: TextView

    private var movieId: Int? = null

    private val viewModel: MovieDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = arguments?.getInt(MOVIE_ID_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.fragment_details_progress_bar)
        ivPoster = view.findViewById(R.id.fragment_details_poster)
        tvOriginalTitle = view.findViewById(R.id.fragment_details_title)
        tvReleaseDate = view.findViewById(R.id.fragment_details_release_date)
        tvVoteAverage = view.findViewById(R.id.fragment_details_vote_average)
        tvOverview = view.findViewById(R.id.fragment_details_overview)
        tvLanguage = view.findViewById(R.id.fragment_details_language)

        // to prevent duplicating calls over configuration change
        if (savedInstanceState == null && movieId != null) {
            viewModel.initDetailsFetch(movieId!!)
        }
        initViewModelInteractions()
    }

    private fun initViewModelInteractions() {
        if (movieId == null) {
            showMovieIdError()
        } else {
            viewModel.movieDetailsUIState.observe(this) { state ->
                showLoading(state.isLoading)
                showNetworkError(state.isError)
                if (state.movie != null) {
                    showMovieDetails(state.movie)
                }
            }
        }
    }

    private fun showMovieDetails(movie: Movie) {
        (activity as MainActivity).supportActionBar?.title = movie.title
        Glide.with(this)
            .load(ConstantsUi.IMAGE_URL + movie.posterPath)
            .into(ivPoster)
        tvOriginalTitle.text = movie.title
        tvReleaseDate.text = movie.releaseDate
        val voteAverageText = movie.voteAverage.toString() + "/10"
        tvVoteAverage.text = voteAverageText
        tvOverview.text = movie.overview
        tvLanguage.text = movie.originalLanguage
    }

    private fun showLoading(isShow: Boolean) {
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun showNetworkError(isError: Boolean) {
        if (isError) {
            AlertDialog.Builder(context)
                .setTitle(R.string.error)
                .setMessage(R.string.error_message_for_network_error)
                .setPositiveButton(R.string.ok) { _, _ ->
                    // do nothing for now
                }
                .show()
        }
    }

    private fun showMovieIdError() {
        AlertDialog.Builder(context)
            .setTitle(R.string.error)
            .setMessage(R.string.error_message_for_missing_movie_id)
            .setPositiveButton(R.string.ok) { _, _ ->
                // do nothing for now
            }
            .show()
    }
}