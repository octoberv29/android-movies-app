package com.example.moviesapp

import androidx.paging.PagingData
import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.data.repository.MoviesRepository
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

/**
 * This is a fake repository that is used only for testing!
 */
class FakeMoviesRepository: MoviesRepository {

    override fun getMoviesRx(): Flowable<PagingData<Movie>> {
        return Flowable.just(
            PagingData.from(listOf(TestStubs.validMovie))
        )
    }

    override fun getMovieDetailsRx(id: Int): Single<Movie> {
        return if (id == TestStubs.validMovieId) {
            Single.just(TestStubs.validMovie)
        } else {
            Single.error(
                HttpException(
                    Response.error<ResponseBody>(
                        404,
                        "Not Found".toResponseBody("plain/text".toMediaTypeOrNull())
                    )
                )
            )
        }
    }

    override suspend fun searchMovieUsingQuery(searchTerm: String): List<Movie>? {
        return if (searchTerm == TestStubs.validSearchTerm) {
            TestStubs.validMovieResponse.listOfMovies
        } else {
            throw HttpException(
                Response.error<ResponseBody>(
                    404,
                    "Not Found".toResponseBody("plain/text".toMediaTypeOrNull())
                )
            )
        }
    }

}