package com.example.moviesapp.ui.details

import com.example.moviesapp.FakeMoviesRepository
import com.example.moviesapp.InstantExecutorExtension
import com.example.moviesapp.TestStubs.Companion.invalidMovieId
import com.example.moviesapp.TestStubs.Companion.validMovie
import com.example.moviesapp.TestStubs.Companion.validMovieId
import com.example.moviesapp.getOrAwaitValue
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode


@Execution(ExecutionMode.CONCURRENT)
@MockKExtension.ConfirmVerification
@ExtendWith(InstantExecutorExtension::class)
internal class MovieDetailsViewModelTest {

    private lateinit var viewModel: MovieDetailsViewModel
    private var moviesRepository: FakeMoviesRepository = FakeMoviesRepository()

    @BeforeEach
    fun setUp() {
        viewModel = MovieDetailsViewModel(moviesRepository)
    }

    @AfterEach
    fun finish() {
        clearAllMocks()
    }

    @Test
    fun `test MovieDetailsViewModel movieDetailsUIState is updated correctly after successful initDetailsFetch call`() {
        viewModel.initDetailsFetch(validMovieId)

        assertEquals(
            MovieDetailsUIState(movie = validMovie, isLoading = false, isError = false),
            viewModel.movieDetailsUIState.getOrAwaitValue()
        )
    }

    @Test
    fun `test MovieDetailsViewModel movieDetailsUIState is updated correctly after HTTP exception`() {
        viewModel.initDetailsFetch(invalidMovieId)

        assertEquals(
            MovieDetailsUIState(movie = null, isLoading = false, isError = true),
            viewModel.movieDetailsUIState.getOrAwaitValue()
        )
    }

}