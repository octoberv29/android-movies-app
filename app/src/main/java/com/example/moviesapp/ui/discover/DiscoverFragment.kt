package com.example.moviesapp.ui.discover

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.di.MoviesApplication
import com.example.moviesapp.ui.DiscoverMoviesAdapter
import com.example.moviesapp.ui.discover.di.DaggerDiscoverFragmentComponent
import com.example.moviesapp.utils.Constants
import javax.inject.Inject

class DiscoverFragment : Fragment() {

    private lateinit var sortBy: String
    private lateinit var discoverMoviesAdapter: DiscoverMoviesAdapter

    private lateinit var rvMovies: RecyclerView

    @Inject
    lateinit var viewModelFactory: DiscoverMoviesViewModel.Companion.DiscoverViewModelFactory
    private val viewModel: DiscoverMoviesViewModel by viewModels { viewModelFactory }

    companion object  {
        private const val NUMBER_OF_COLUMNS = 3
        private const val SORT_BY_KEY = "sortBy"
        private const val DEFAULT_SORTING = "popularity.desc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inject

        DaggerDiscoverFragmentComponent.builder()
            .appComponent((context?.applicationContext as MoviesApplication).appComponent)
            .build()
            .inject(this)

        sortBy = arguments?.getString(SORT_BY_KEY, Constants.EMPTY_STRING) ?: DEFAULT_SORTING
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
        discoverMoviesAdapter = DiscoverMoviesAdapter()
        rvMovies.adapter = discoverMoviesAdapter

        viewModel.initTrending(sortBy, 1)

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
}