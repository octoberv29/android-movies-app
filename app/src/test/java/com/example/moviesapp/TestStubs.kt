package com.example.moviesapp

import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.data.network.MovieResponse

class TestStubs {
    companion object {
        const val invalidMovieId = 0
        const val validMovieId = 12345
        const val validSearchTerm = "search"
        val validMovie = Movie(
            id = validMovieId,
            originalLanguage = "en",
            originalTitle = "Movie"
        )
        val validMovieResponse = MovieResponse(
            totalPages = 100,
            listOfMovies = listOf(validMovie)
        )
        const val sorting = "popularity.desc"
        const val startPage = 1
    }
}