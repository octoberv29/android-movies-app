package com.example.moviesapp.ui.discover

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.ui.ConstantsUi.Companion.MOVIE_ID_KEY
import com.example.moviesapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * DiscoverMoviesFragment is responsible for showing a list of popular movies
 */
@AndroidEntryPoint
class DiscoverMoviesFragment : Fragment() {

    private lateinit var rvMovies: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var discoverMoviesAdapter: DiscoverMoviesAdapter

    private val viewModel: DiscoverMoviesViewModel by viewModels()

    companion object  {
        private const val DEFAULT_NUMBER_OF_COLUMNS = 3
        const val DEFAULT_DISCOVER_MOVIES_ACTION_BAR_TITLE = "Discover Popular Movies"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).supportActionBar?.title = DEFAULT_DISCOVER_MOVIES_ACTION_BAR_TITLE

        progressBar = view.findViewById(R.id.fragment_discover_movies_progress_bar)
        rvMovies = view.findViewById(R.id.fragment_discover_movies_recycler_view)
        rvMovies.layoutManager = GridLayoutManager(activity, DEFAULT_NUMBER_OF_COLUMNS)
        rvMovies.setHasFixedSize(true)
        discoverMoviesAdapter = DiscoverMoviesAdapter(this::onMovieClick)
        discoverMoviesAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        discoverMoviesAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Error) {
                showNetworkError(true)
            }
        }
        rvMovies.adapter = discoverMoviesAdapter
        initViewModelInteractions()
        // to prevent duplicating calls over configuration change
        if (savedInstanceState == null) {
            viewModel.initDiscovering()
        }
    }

    private fun initViewModelInteractions() {
        viewModel.discoverMoviesUiState.observe(viewLifecycleOwner) { state ->
            showLoading(state.isLoading)
            showNetworkError(state.isError)
            if (state.movies != null) {
                showMovies(state.movies)
            }
        }
    }

    private fun showMovies(movies: PagingData<Movie>) {
        discoverMoviesAdapter.submitData(lifecycle, movies)
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

    private fun showLoading(isShow: Boolean) {
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun onMovieClick(movieId: Int) {
        val bundle = bundleOf(MOVIE_ID_KEY to movieId)
        findNavController().navigate(R.id.action_discoverFragment_to_detailsFragment, bundle)
    }
}