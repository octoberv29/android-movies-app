package com.example.moviesapp.ui.discover

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.moviesapp.data.repository.MoviesRepository
import com.example.moviesapp.data.network.Movie
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * DiscoverMoviesUiState is a UI state used in the DiscoverMoviesViewModel
 */
data class DiscoverMoviesUiState(
    val movies: PagingData<Movie>?,
    val isLoading: Boolean,
    val isError: Boolean
)

/**
 * DiscoverMoviesViewModel is a ViewModel class associated with DiscoverMoviesFragment
 */
class DiscoverMoviesViewModel(
    private val moviesRepository: MoviesRepository,
) : ViewModel() {

    private var moviesDisposable: Disposable? = null

    private val _discoverMoviesUiState = MutableLiveData<DiscoverMoviesUiState>()
    val discoverMoviesUiState: LiveData<DiscoverMoviesUiState>
        get() = _discoverMoviesUiState

    @OptIn(ExperimentalCoroutinesApi::class)
    fun initDiscovering() {
        handleLoading()
        moviesDisposable = moviesRepository.getMoviesRx()
            .cachedIn(viewModelScope)
            .subscribe(
                this::handleMovies,
                this::handleError
            )
    }

    internal fun handleLoading() {
        _discoverMoviesUiState.value = DiscoverMoviesUiState(
            movies = null,
            isLoading = true,
            isError = false,
        )
    }

    internal fun handleMovies(data: PagingData<Movie>) {
        _discoverMoviesUiState.value = DiscoverMoviesUiState(
            movies = data,
            isLoading = false,
            isError = false,
        )
    }

    internal fun handleError(t: Throwable) {
        _discoverMoviesUiState.value = DiscoverMoviesUiState(
            movies = null,
            isLoading = false,
            isError = true,
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
                return DiscoverMoviesViewModel(
                    moviesRepository
                ) as T
            }
        }
    }
}