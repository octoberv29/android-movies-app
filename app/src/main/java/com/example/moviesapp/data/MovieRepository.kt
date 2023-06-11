package com.example.moviesapp.data

import com.example.moviesapp.data.network.Movie
import io.reactivex.Single

interface MoviesRepository {

    fun getMoviesRx(sortBy: String?, page: Int): Single<List<Movie>?>

    fun getMovieDetailsRx(id: Int): Single<Movie>
}