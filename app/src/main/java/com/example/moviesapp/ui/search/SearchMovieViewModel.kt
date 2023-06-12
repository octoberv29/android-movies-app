package com.example.moviesapp.ui.search

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.moviesapp.data.MoviesRepository
import com.example.moviesapp.data.network.Movie
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SearchMovieUIState is a UI state for the SearchMovieViewModel
 */
data class SearchMovieUIState(
    val movies: List<Movie>?,
    val isEmpty: Boolean,
    val isLoading: Boolean,
    val isError: Boolean
)

/**
 * SearchMovieViewModel is a ViewModel associated with a SearchMovieFragment
 */
class SearchMovieViewModel(
    private val moviesRepository: MoviesRepository
): ViewModel() {

    private val _searchMovieUIState = MutableLiveData<SearchMovieUIState>()
    val searchMovieUIState: LiveData<SearchMovieUIState>
        get() = _searchMovieUIState

    fun initSearch(searchTerm: String) {
        try {
            handleLoading()
            viewModelScope.launch {
                val movies = moviesRepository.searchMovieUsingQuery(searchTerm)
                if (movies == null) {
                    _searchMovieUIState.value = SearchMovieUIState(
                        movies = null,
                        isEmpty = false,
                        isLoading = false,
                        isError = true
                    )
                } else if (movies.isEmpty()) {
                    _searchMovieUIState.value = SearchMovieUIState(
                        movies = null,
                        isEmpty = true,
                        isLoading = false,
                        isError = false
                    )
                } else {
                    _searchMovieUIState.value = SearchMovieUIState(
                        movies = movies,
                        isEmpty = false,
                        isLoading = false,
                        isError = false
                    )
                }
            }
        } catch (t: Throwable) {
            _searchMovieUIState.value = SearchMovieUIState(
                movies = null,
                isEmpty = false,
                isLoading = false,
                isError = true
            )
        }
    }

    private fun handleLoading() {
        _searchMovieUIState.value = SearchMovieUIState(
            movies = null,
            isEmpty = false,
            isLoading = true,
            isError = false
        )
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