package com.example.moviesapp.ui.details

import androidx.lifecycle.*
import com.example.moviesapp.data.repository.MoviesRepository
import com.example.moviesapp.data.network.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * MovieDetailsUIState is a UI state for the MovieDetailsViewModel
 */
data class MovieDetailsUIState(
    val movie: Movie?,
    val isLoading: Boolean,
    val isError: Boolean
)

/**
 * MovieDetailsViewModel is a ViewModel associated with a MovieDetailsFragment
 */
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _movieDetailsUIState = MutableLiveData<MovieDetailsUIState>()
    val movieDetailsUIState: LiveData<MovieDetailsUIState>
        get() = _movieDetailsUIState

    private var getMovieDetailDisposable: Disposable? = null

    fun initDetailsFetch(movieId: Int) {
        _movieDetailsUIState.value = MovieDetailsUIState(
            movie = null,
            isLoading = true,
            isError = false
        )
        getMovieDetailDisposable = getMovieDetailsRx(movieId)
    }

    private fun getMovieDetailsRx(movieId: Int): Disposable {
        return moviesRepository.getMovieDetailsRx(movieId)
            .subscribe(
                this::handleMovieDetails,
                this::handleError
            )

    }

    private fun handleMovieDetails(movie: Movie) {
        _movieDetailsUIState.value = MovieDetailsUIState(
            movie = movie,
            isLoading = false,
            isError = false
        )
    }

    private fun handleError(t: Throwable) {
        _movieDetailsUIState.value = MovieDetailsUIState(
            movie = null,
            isLoading = false,
            isError = true
        )
    }

    override fun onCleared() {
        super.onCleared()
        getMovieDetailDisposable?.dispose()
    }
}