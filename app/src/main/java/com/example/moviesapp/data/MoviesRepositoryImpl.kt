package com.example.moviesapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.data.network.MovieApi
import com.example.moviesapp.data.network.MovieResponse
import com.example.moviesapp.data.paging.GetMoviesRxPagingSource
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepositoryImp(
    private val movieApiService: MovieApi,
    private val getMoviesRxPagingSource: GetMoviesRxPagingSource
): MoviesRepository {

    override fun getMoviesRx(): Flowable<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = { getMoviesRxPagingSource }
        ).flowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMovieDetailsRx(id: Int): Single<Movie> {
        return movieApiService.getMovieDetailsRx(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override suspend fun searchMovieUsingQuery(searchTerm: String): List<Movie>? {
        return withContext(Dispatchers.IO) {
            val movieResponse = movieApiService.searchMovieUsingQuery(searchTerm)
            movieResponse.listOfMovies
        }
    }

}