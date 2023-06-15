package com.example.moviesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.data.network.MovieApi
import com.example.moviesapp.data.paging.GetMoviesRxPagingSource
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepositoryImpl(
    private val movieApiService: MovieApi,
    private val getMoviesRxPagingSource: GetMoviesRxPagingSource,
    private val ioScheduler: Scheduler = Schedulers.io(),
    private val mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()
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
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
    }

    override fun getMovieDetailsRx(id: Int): Single<Movie> {
        return movieApiService.getMovieDetailsRx(id)
            .subscribeOn(ioScheduler)
            .observeOn(mainThreadScheduler)
    }

    override suspend fun searchMovieUsingQuery(searchTerm: String): List<Movie>? {
        return withContext(Dispatchers.IO) {
            val movieResponse = movieApiService.searchMovieUsingQuery(searchTerm)
            movieResponse.listOfMovies
        }
    }

}