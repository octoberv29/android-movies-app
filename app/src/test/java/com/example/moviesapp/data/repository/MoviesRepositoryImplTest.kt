package com.example.moviesapp.data.repository

import com.example.moviesapp.TestStubs.Companion.invalidMovieId
import com.example.moviesapp.TestStubs.Companion.sorting
import com.example.moviesapp.TestStubs.Companion.startPage
import com.example.moviesapp.TestStubs.Companion.validMovie
import com.example.moviesapp.TestStubs.Companion.validMovieId
import com.example.moviesapp.TestStubs.Companion.validMovieResponse
import com.example.moviesapp.TestStubs.Companion.validSearchTerm
import com.example.moviesapp.data.network.MovieApi
import com.example.moviesapp.data.paging.GetMoviesRxPagingSource
import com.example.moviesapp.data.repository.di.DaggerTestMoviesRepositoryImplComponent
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@Execution(ExecutionMode.CONCURRENT)
@MockKExtension.ConfirmVerification
@OptIn(ExperimentalCoroutinesApi::class)
class MoviesRepositoryImplTest {

    @Inject
    lateinit var moviesRepository: MoviesRepositoryImpl

    @Inject
    lateinit var movieApiService: MovieApi

    @Inject
    lateinit var getMoviesRxPagingSource: GetMoviesRxPagingSource

    @BeforeEach
    fun setUp() {
        DaggerTestMoviesRepositoryImplComponent.create().inject(this)
    }

    @AfterEach
    fun finish() {
        clearAllMocks()
    }

    @Test
    fun `test MoviesRepositoryImplTest getMoviesRx returns correct Flowable`() {
        every {
            movieApiService.getMoviesRx(sorting, startPage)
        } returns Single.just(validMovieResponse)
        every {
            getMoviesRxPagingSource.registerInvalidatedCallback(any())
        } returns Unit

        moviesRepository.getMoviesRx()
            .test()
            .awaitDone(1, TimeUnit.SECONDS)
            .assertNoErrors()
    }

    @Test
    fun `test MoviesRepositoryImplTest getMovieDetailsRx with valid id returns correct Movie`() {
        every {
            movieApiService.getMovieDetailsRx(validMovieId)
        } returns Single.just(validMovie)

        moviesRepository.getMovieDetailsRx(validMovieId)
            .test()
            .awaitDone(1, TimeUnit.SECONDS)
            .assertNoErrors()
            .assertComplete()
            .assertResult(validMovie)

        verify(exactly = 1) { movieApiService.getMovieDetailsRx(validMovieId) }
    }

    @Test
    fun `test MoviesRepositoryImplTest getMovieDetailsRx with invalid id throws HTTP error`() {
        every {
            movieApiService.getMovieDetailsRx(invalidMovieId)
        } returns Single.error(
            HttpException(
                Response.error<ResponseBody>(
                    404,
                    "Not Found".toResponseBody("plain/text".toMediaTypeOrNull())
                )
            )
        )

        moviesRepository.getMovieDetailsRx(invalidMovieId)
            .test()
            .awaitDone(1, TimeUnit.SECONDS)
            .assertError(HttpException::class.java)

        verify(exactly = 1) { movieApiService.getMovieDetailsRx(invalidMovieId) }
    }

    @Test
    fun `test MoviesRepositoryImplTest searchMovieUsingQuery with valid searchTerm returns correct list of Movie`() {
        runTest {
            coEvery { movieApiService.searchMovieUsingQuery(validSearchTerm) } returns validMovieResponse

            val result = moviesRepository.searchMovieUsingQuery(validSearchTerm)

            assertEquals(listOf(validMovie), result)
            coVerify(exactly = 1) { movieApiService.searchMovieUsingQuery(validSearchTerm) }
        }
    }

    @org.junit.Test(expected = Exception::class)
    fun `test MoviesRepositoryImplTest searchMovieUsingQuery throws HTTP Exception`() {
        runTest {
            coEvery {
                movieApiService.searchMovieUsingQuery(validSearchTerm)
            } throws HttpException(
                Response.error<ResponseBody>(
                    404,
                    "Not Found".toResponseBody("plain/text".toMediaTypeOrNull())
                )
            )

            moviesRepository.searchMovieUsingQuery(validSearchTerm)

            coVerify(exactly = 1) { movieApiService.searchMovieUsingQuery(validSearchTerm) }
        }
    }

}