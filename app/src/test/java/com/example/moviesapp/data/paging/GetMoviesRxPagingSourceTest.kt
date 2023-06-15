package com.example.moviesapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingSource.*
import androidx.paging.PagingState
import com.example.moviesapp.InstantExecutorExtension
import com.example.moviesapp.TestStubs
import com.example.moviesapp.data.network.Movie
import com.example.moviesapp.data.network.MovieApi
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkClass
import io.reactivex.Single
import io.reactivex.internal.schedulers.TrampolineScheduler
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import retrofit2.HttpException
import retrofit2.Response


@Execution(ExecutionMode.CONCURRENT)
@MockKExtension.ConfirmVerification
@ExtendWith(InstantExecutorExtension::class)
internal class GetMoviesRxPagingSourceTest {

    private lateinit var getMoviesRxPagingSource: GetMoviesRxPagingSource
    private lateinit var movieApiService: MovieApi

    @BeforeEach
    fun setUp() {
        movieApiService = mockkClass(MovieApi::class)
        getMoviesRxPagingSource = GetMoviesRxPagingSource(
            movieApiService,
            TrampolineScheduler.instance()
        )
    }

    @AfterEach
    fun finish() {
        clearAllMocks()
    }

    @Test
    fun `test GetMoviesRxPagingSource loadSingle with validMovieResponse`() {
        val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
            PagingSource.LoadParams.Refresh(TestStubs.startPage, 2, false)

        every {
            movieApiService.getMoviesRx(TestStubs.sorting, TestStubs.startPage)
        } returns Single.just(TestStubs.validMovieResponse)

        getMoviesRxPagingSource.loadSingle(refreshRequest)
            .test()
            .await()
            .assertValueCount(1)
            .assertValue(
                LoadResult.Page<Int, Movie>(
                    TestStubs.validMovieResponse.listOfMovies!!,
                    null,
                    2
                )
            )
    }

    @Test
    fun `test GetMoviesRxPagingSource loadSingle with HTTP exception`() {
        val refreshRequest: PagingSource.LoadParams.Refresh<Int> =
            PagingSource.LoadParams.Refresh(TestStubs.startPage, 2, false)

        val httpException = HttpException(
            Response.error<ResponseBody>(
                404,
                "Not Found".toResponseBody("plain/text".toMediaTypeOrNull())
            )
        )

        every {
            movieApiService.getMoviesRx(TestStubs.sorting, TestStubs.startPage)
        } returns Single.error(httpException)

        getMoviesRxPagingSource.loadSingle(refreshRequest)
            .test()
            .await()
            .assertValueCount(1)
            .assertValue(LoadResult.Error<Int, Movie>(httpException))
    }

    @Test
    fun `test GetMoviesRxPagingSource getRefreshKey returns null when anchorPosition is null`() {
        val state = mockk<PagingState<Int, Movie>>()
        every {
            state.anchorPosition
        } returns null

        val result = getMoviesRxPagingSource.getRefreshKey(state)
        assertEquals(null, result)
    }

    @Test
    fun `test GetMoviesRxPagingSource getRefreshKey returns null when anchorPage is null`() {
        val state = mockk<PagingState<Int, Movie>>()
        every {
            state.anchorPosition
        } returns 1
        every {
            state.closestPageToPosition(1)
        } returns null

        val result = getMoviesRxPagingSource.getRefreshKey(state)
        assertEquals(null, result)
    }

    @Test
    fun `test GetMoviesRxPagingSource getRefreshKey returns null when anchorPage prevKey and nextKey are null`() {
        val state = mockk<PagingState<Int, Movie>>()
        every {
            state.anchorPosition
        } returns 1
        every {
            state.closestPageToPosition(1)
        } returns LoadResult.Page(listOf(), null, null)

        val result = getMoviesRxPagingSource.getRefreshKey(state)
        assertEquals(null, result)
    }

    @Test
    fun `test GetMoviesRxPagingSource getRefreshKey returns when anchorPage prevKey is not null`() {
        val state = mockk<PagingState<Int, Movie>>()
        every {
            state.anchorPosition
        } returns 1
        every {
            state.closestPageToPosition(1)
        } returns LoadResult.Page(listOf(), 2, 3)

        val result = getMoviesRxPagingSource.getRefreshKey(state)
        assertEquals(3, result)
    }

    @Test
    fun `test GetMoviesRxPagingSource getRefreshKey returns when anchorPage nextKey is not null`() {
        val state = mockk<PagingState<Int, Movie>>()
        every {
            state.anchorPosition
        } returns 1
        every {
            state.closestPageToPosition(1)
        } returns LoadResult.Page(listOf(), null, 3)

        val result = getMoviesRxPagingSource.getRefreshKey(state)
        assertEquals(2, result)
    }

}