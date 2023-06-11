package com.example.moviesapp.ui.discover

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.moviesapp.data.MoviesRepository
import com.example.moviesapp.data.network.Movie
import io.reactivex.disposables.Disposable
import javax.inject.Inject

data class DiscoverMoviesUiState(
    val movies: List<Movie>?,
    val isEmpty: Boolean,
    val isLoading: Boolean,
    val isError: Boolean
)

class DiscoverMoviesViewModel(
    // TODO: save state
    private val savedStateHandle: SavedStateHandle,
    private val moviesRepository: MoviesRepository,
) : ViewModel() {

    private var moviesDisposable: Disposable? = null

    private val _discoverMoviesUiState = MutableLiveData<DiscoverMoviesUiState>()
    val discoverMoviesUiState: LiveData<DiscoverMoviesUiState>
        get() = _discoverMoviesUiState


    fun initDiscovering(sortBy: String, page: Int) {
        moviesDisposable = moviesRepository.getMoviesRx(sortBy, page)
            .subscribe(
                this::handleMovies,
                this::handleError
            )
    }

    private fun handleMovies(listOfMovies: List<Movie>?) {
        if (listOfMovies != null && listOfMovies.isNotEmpty()) {
            _discoverMoviesUiState.value = DiscoverMoviesUiState(
                movies = listOfMovies,
                isEmpty = false,
                isLoading = false,
                isError = false
            )
        } else {
            handleEmptyListOfMovies()
        }
    }

    private fun handleEmptyListOfMovies() {
        _discoverMoviesUiState.value = DiscoverMoviesUiState(
            movies = null,
            isEmpty = true,
            isLoading = false,
            isError = true
        )
    }

    private fun handleError(t: Throwable) {
        // TODO: add message to the handle error
        _discoverMoviesUiState.value = DiscoverMoviesUiState(
            movies = null,
            isEmpty = false,
            isLoading = false,
            isError = true
        )
    }

    override fun onCleared() {
        super.onCleared()
        moviesDisposable?.dispose()
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        class DiscoverMoviesViewModelFactory @Inject constructor(
            private val moviesRepository: MoviesRepository
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return DiscoverMoviesViewModel(
                    savedStateHandle,
                    moviesRepository
                ) as T
            }
        }
    }
}