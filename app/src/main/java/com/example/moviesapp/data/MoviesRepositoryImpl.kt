package com.example.moviesapp.data

import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.data.network.MovieApi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesRepositoryImp @Inject constructor(
    private val retrofitService: MovieApi
): MoviesRepository {

    override fun getMoviesRx(sortBy: String?, page: Int): Single<List<Movie>?> {
        return retrofitService.getMoviesRx(sortBy, page)
            .subscribeOn(Schedulers.io())
            .map { it.movies }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMovieDetailsRx(id: Int): Single<Movie> {
        return retrofitService.getMovieDetailsRx(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}