package com.example.moviesapp.data.network

import com.google.gson.annotations.SerializedName

data class MovieResponse (
    @SerializedName("page")
    val page: Int? = 0,

    @SerializedName("total_results")
    val totalResults: Int? = 0,

    @SerializedName("total_pages")
    val totalPages: Int? = 0,

    @SerializedName("results")
    val listOfMovies: List<Movie>? = null
)

data class Movie (

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("adult")
    val isAdult: Boolean? = null,

    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @SerializedName("genre_ids")
    var genreIds: List<Int>? = null,

    @SerializedName("original_language")
    var originalLanguage: String? = null,

    @SerializedName("original_title")
    var originalTitle: String? = null,

    @SerializedName("overview")
    var overview: String? = null,

    @SerializedName("popularity")
    var popularity: Double? = null,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("release_date")
    var releaseDate: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("video")
    var isVideo: Boolean? = null,

    @SerializedName("vote_average")
    var voteAverage: Double? = null,

    @SerializedName("vote_count")
    var voteCount: Double? = null
)
