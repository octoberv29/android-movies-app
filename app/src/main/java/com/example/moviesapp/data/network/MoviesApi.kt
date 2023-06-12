package com.example.moviesapp.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("discover/movie")
    fun getMoviesRx(
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int
    ): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetailsRx(
        @Path("movie_id") id: Int
    ): Single<Movie>
}