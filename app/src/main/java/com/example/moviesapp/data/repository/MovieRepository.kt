package com.example.moviesapp.data.repository

import androidx.paging.PagingData
import com.example.moviesapp.data.network.Movie
import io.reactivex.Flowable
import io.reactivex.Single

interface MoviesRepository {

    fun getMoviesRx(): Flowable<PagingData<Movie>>

    fun getMovieDetailsRx(id: Int): Single<Movie>

    suspend fun searchMovieUsingQuery(searchTerm: String): List<Movie>?
}