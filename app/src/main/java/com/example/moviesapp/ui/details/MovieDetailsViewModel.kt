package com.example.moviesapp.ui.details

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.moviesapp.data.MoviesRepository
import com.example.moviesapp.data.network.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
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
class MovieDetailsViewModel(
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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

    companion object {
        @Suppress("UNCHECKED_CAST")
        class MovieDetailsViewModelFactory @Inject constructor(
            private val moviesRepository: MoviesRepository,
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return MovieDetailsViewModel(
                    moviesRepository
                ) as T
            }
        }
    }
}