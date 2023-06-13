package com.example.moviesapp.ui.search

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.data.di.MoviesApplication
import com.example.moviesapp.ui.MainActivity
import com.example.moviesapp.ui.search.di.DaggerSearchMovieFragmentComponent
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchMovieFragment: Fragment() {

    private lateinit var etSearchTerm: TextInputEditText
    private lateinit var rvSearchResults: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var adapter: SearchMovieAdapter

    companion object {
        private const val DEFAULT_SEARCH_ACTION_BAR_TITLE = "Search movie"
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

        progressBar = view.findViewById(R.id.fragment_search_progress_bar)
        etSearchTerm = view.findViewById(R.id.fragment_search_search_text_input_edit_text)
        rvSearchResults = view.findViewById(R.id.fragment_search_search_results_recycler_view)
        rvSearchResults.layoutManager = LinearLayoutManager(activity)
        rvSearchResults.setHasFixedSize(true)
        adapter = SearchMovieAdapter(this::onMovieClick)
        rvSearchResults.adapter = adapter

        initViewModelInteractions()
    }

    private fun initViewModelInteractions() {
        etSearchTerm.addTextChangedListener {
            viewModel.initSearch(it.toString())
        }

        lifecycleScope.launch {
            viewModel.searchMovieUIState.collect { state ->
                showLoading(state.isLoading)
                showNetworkError(state.isError)
                if (state.movies != null) {
                    adapter.setMovies(state.movies)
                }
            }
        }
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
        // TODO: magic string
        val bundle = bundleOf("movie_id" to movieId)
        findNavController().navigate(R.id.action_searchMovieFragment_to_detailsFragment, bundle)
    }
}