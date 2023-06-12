package com.example.moviesapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.moviesapp.R
import com.example.moviesapp.di.MoviesApplication
import com.example.moviesapp.ui.MainActivity
import com.example.moviesapp.ui.search.di.DaggerSearchMovieFragmentComponent
import javax.inject.Inject

class SearchMovieFragment: Fragment() {

    companion object {
        private const val TESTING_SEARCH_TERM = "spider"
        private const val DEFAULT_SEARCH_ACTION_BAR_TITLE = "Search"
    }

    @Inject
    lateinit var viewModelFactory: SearchMovieViewModel.Companion.SearchMovieViewModelFactory
    private val viewModel: SearchMovieViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerSearchMovieFragmentComponent.builder()
            .appComponent((activity?.applicationContext as MoviesApplication).appComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = DEFAULT_SEARCH_ACTION_BAR_TITLE

        initViewModelInteractions()
    }

    private fun initViewModelInteractions() {
        viewModel.initSearch(TESTING_SEARCH_TERM)
        viewModel.searchMovieUIState.observe(this) { state ->
            // TODO: add loading
            // TODO: add error
            // TODO: add empty search
            if (state.movies != null) {
                // populate recycler view
                println("HERE: listOfMovies=${state.movies}")
            }
        }
    }
}