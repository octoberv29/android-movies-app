package com.example.moviesapp.data

import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.data.network.MovieApi
import com.example.moviesapp.data.network.MovieResponse
import io.reactivex.Single
import javax.inject.Inject

class MoviesRepositoryImp @Inject constructor(
    private val retrofitService: MovieApi
): MoviesRepository {

    override fun getMoviesRx(sortBy: String?, page: Int): Single<MovieResponse> {
        return retrofitService.getMoviesRx(sortBy, page)
    }

    override fun getMovieDetailsRx(id: Int): Single<Movie> {
        return retrofitService.getMovieDetailsRx(id)
    }

}