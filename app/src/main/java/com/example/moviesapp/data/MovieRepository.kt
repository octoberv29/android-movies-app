package com.example.moviesapp.data

import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.data.network.MovieResponse
import io.reactivex.Single

interface MoviesRepository {

    fun getMoviesRx(sortBy: String?, page: Int): Single<MovieResponse>

    fun getMovieDetailsRx(id: Int): Single<Movie>
}