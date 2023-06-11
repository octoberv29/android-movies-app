package com.example.moviesapp.data.network

import com.google.gson.annotations.SerializedName

data class Movie (

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("popularity")
    var popularity: Double? = null,

    @SerializedName("vote_count")
    var voteCount: Double? = null,

    @SerializedName("video")
    var isVideo: Boolean? = null,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("adult")
    var isAdult: Boolean? = null,

    @SerializedName("backdrop_path")
    var backdropPath: String? = null,

    @SerializedName("original_language")
    var originalLanguage: String? = null,

    @SerializedName("original_title")
    var originalTitle: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("vote_average")
    var voteAverage: Double? = null,

    @SerializedName("overview")
    var overview: String? = null,

    @SerializedName("release_date")
    var releaseDate: String? = null
)
