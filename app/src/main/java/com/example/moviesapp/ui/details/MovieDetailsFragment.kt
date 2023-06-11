package com.example.moviesapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.di.MoviesApplication
import com.example.moviesapp.ui.details.di.DaggerMovieDetailsFragmentComponent
import com.example.moviesapp.utils.Constants
import javax.inject.Inject

class MovieDetailsFragment : Fragment() {

    private lateinit var ivPoster: ImageView
    private lateinit var tvOriginalTitle: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvVoteAverage: TextView
    private lateinit var tvOverview: TextView
    private lateinit var tvLanguage: TextView


    private var movieId: Int = -1

    @Inject
    lateinit var viewModelFactory: MovieDetailsViewModel.Companion.MovieDetailsViewModelFactory
    private val viewModel: MovieDetailsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerMovieDetailsFragmentComponent.builder()
            .appComponent((context?.applicationContext as MoviesApplication).appComponent)
            .build()
            .inject(this)

        // TODO: magic string and default param
        // TODO: what is movie id is -1???
        movieId = arguments?.getInt("movie_id") ?: -1
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

        ivPoster = view.findViewById(R.id.ivPoster)
        tvOriginalTitle = view.findViewById(R.id.tvOriginalTitle)
        tvReleaseDate = view.findViewById(R.id.tvReleaseDate)
        tvVoteAverage = view.findViewById(R.id.tvVoteAverage)
        tvOverview = view.findViewById(R.id.tvOverview)
        tvLanguage = view.findViewById(R.id.tvLanguage)

        initViewModelInteractions()
    }

    private fun initViewModelInteractions() {
        viewModel.init(movieId)

        viewModel.movieDetailsUIState.observe(this) { state ->

            // TODO:
            if (state.isLoading) {
                // show loading
            } else if (state.isError) {
                // show error
            } else if (state.movie == null) {
                //
            } else {
                // show
                val movie = state.movie
                Glide.with(this)
                    .load(Constants.IMAGE_URL + movie.posterPath)
                    .into(ivPoster)
                tvOriginalTitle.text = movie.title
                tvReleaseDate.text = movie.releaseDate
                // TODO: magic string
                tvVoteAverage.text = movie.voteAverage.toString() + "/10"
                tvOverview.text = movie.overview
                tvLanguage.text = movie.originalLanguage

            }
        }
    }
}