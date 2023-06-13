package com.example.moviesapp.ui.search

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.moviesapp.data.MoviesRepository
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
                    _searchMovieUIState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                } else {
                    _searchMovieUIState.update { currentState ->
                        currentState.copy(
                            movies = movies,
                            isLoading = false
                        )
                    }
                }
            }
        } catch (t: Throwable) {
            _searchMovieUIState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    isError = true
                )
            }
        }
    }

    private fun handleLoading() {
        _searchMovieUIState.update { currentState ->
            currentState.copy(
                isLoading = true
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