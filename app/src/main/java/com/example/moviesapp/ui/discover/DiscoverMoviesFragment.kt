package com.example.moviesapp.ui.discover

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.di.MoviesApplication
import com.example.moviesapp.ui.DiscoverMoviesAdapter
import com.example.moviesapp.ui.discover.di.DaggerDiscoverMoviesFragmentComponent
import javax.inject.Inject

class DiscoverMoviesFragment : Fragment() {

    private lateinit var rvMovies: RecyclerView
    private lateinit var discoverMoviesAdapter: DiscoverMoviesAdapter

    @Inject
    lateinit var viewModelFactory: DiscoverMoviesViewModel.Companion.DiscoverMoviesViewModelFactory
    private val viewModel: DiscoverMoviesViewModel by viewModels { viewModelFactory }

    companion object  {
        private const val DEFAULT_SORTING = "popularity.desc"
        private const val DEFAULT_START_PAGE = 1
        private const val NUMBER_OF_COLUMNS = 3
        private const val DEFAULT_DISCOVER_MOVIES_APPBAR_TITLE = "Discover Popular Movies"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        DaggerDiscoverMoviesFragmentComponent.builder()
            .appComponent((context?.applicationContext as MoviesApplication).appComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvMovies = view.findViewById(R.id.discover_movies_recycler_view_movies)
        rvMovies.layoutManager = GridLayoutManager(activity, NUMBER_OF_COLUMNS)
        rvMovies.setHasFixedSize(true)
        discoverMoviesAdapter = DiscoverMoviesAdapter(this::onMovieClick)
        rvMovies.adapter = discoverMoviesAdapter

        // TODO; magic string
        activity?.actionBar?.title = DEFAULT_DISCOVER_MOVIES_APPBAR_TITLE
        

        initViewModelInteractions()
    }

    private fun initViewModelInteractions() {
        viewModel.initDiscovering(DEFAULT_SORTING, DEFAULT_START_PAGE)

        viewModel.discoverMoviesUiState.observe(viewLifecycleOwner) { state ->
            // TODO: do we really want thi logic here?
            if (state.isEmpty) {
                showEmptyView()
            } else if (state.isError) {
                showNetworkError()
            } else {
                discoverMoviesAdapter.setMovies(state.movies!!)
            }
        }
    }

    private fun showNetworkError() {
        AlertDialog.Builder(context)
            .setTitle(R.string.error)
            .setMessage(R.string.error_message)
            .setPositiveButton(R.string.ok) { _, _ ->
                // do nothing for now
            }
            .show()
    }

    private fun showEmptyView() {
        // TODO: show empty view
        // do nothing for now
    }

    private fun onMovieClick(movieId: Int) {
        val bundle = bundleOf("movie_id" to movieId)
        findNavController().navigate(R.id.action_discoverFragment_to_detailsFragment, bundle)
    }
}