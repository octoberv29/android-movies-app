package com.example.moviesapp.ui.search

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.moviesapp.data.repository.MoviesRepository
import com.example.moviesapp.data.network.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SearchMovieUIState is a UI state for the SearchMovieViewModel
 */
data class SearchMovieUIState(
    val movies: List<Movie>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

/**
 * SearchMovieViewModel is a ViewModel associated with a SearchMovieFragment
 */
class SearchMovieViewModel(
    private val moviesRepository: MoviesRepository
): ViewModel() {

    private val _searchMovieUIState = MutableStateFlow(SearchMovieUIState())
    val searchMovieUIState: StateFlow<SearchMovieUIState> = _searchMovieUIState.asStateFlow()

    fun initSearch(searchTerm: String) {
        handleLoading()
        try {
            viewModelScope.launch {
                val movies = if (searchTerm.isNotBlank()) {
                    moviesRepository.searchMovieUsingQuery(searchTerm)
                } else {
                    emptyList()
                }
                if (movies == null) {
                    handleError()
                } else {
                    handleMovies(movies)
                }
            }
        } catch (e: Exception) {
            handleError()
        }
    }

    internal fun handleMovies(movies: List<Movie>) {
        _searchMovieUIState.update { currentState ->
            currentState.copy(
                movies = movies,
                isLoading = false
            )
        }
    }

    internal fun handleLoading() {
        _searchMovieUIState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
    }

    internal fun handleError() {
        _searchMovieUIState.update { currentState ->
            currentState.copy(
                isLoading = false,
                isError = true
            )
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        class SearchMovieViewModelFactory @Inject constructor(
            private val moviesRepository: MoviesRepository,
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return SearchMovieViewModel(
                    moviesRepository
                ) as T
            }
        }
    }
}