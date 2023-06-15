package com.example.moviesapp.ui.discover

import androidx.paging.PagingData
import com.example.moviesapp.FakeMoviesRepository
import com.example.moviesapp.InstantExecutorExtension
import com.example.moviesapp.TestStubs.Companion.validMovie
import com.example.moviesapp.getOrAwaitValue
import io.mockk.*
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
internal class DiscoverMoviesViewModelTest {

    private lateinit var viewModel: DiscoverMoviesViewModel
    private var moviesRepository: FakeMoviesRepository = FakeMoviesRepository()

    @BeforeEach
    fun setUp() {
        viewModel = DiscoverMoviesViewModel(moviesRepository)
    }

    @AfterEach
    fun finish() {
        clearAllMocks()
    }

    // TODO: I was not able to test initDiscovering properly, because of the .cachedIn(viewModelScope).
    //  To test the rest of the class I decided to change access modifiers from private to
    //  internal which should be changed back to private as soon as I find the way to test
    //  initDiscovering properly and avoid breaking on .cachedIn(viewModelScope)
//    @Test
//    fun `DiscoverMoviesViewModel initDiscovering`() {
//        viewModel.initDiscovering()
//
//        assertEquals(
//            DiscoverMoviesUiState(movies = null, isLoading = false, isError = false),
//            viewModel.discoverMoviesUiState.getOrAwaitValue()
//        )
//    }

    @Test
    fun `test DiscoverMoviesViewModel handleLoading sets the discoverMoviesUiState correctly`() {
        viewModel.handleLoading()
        assertEquals(
            DiscoverMoviesUiState(movies = null, isLoading = true, isError = false),
            viewModel.discoverMoviesUiState.getOrAwaitValue()
        )
    }

    @Test
    fun `test DiscoverMoviesViewModel handleMovies sets the discoverMoviesUiState correctly`() {
        val data = PagingData.from(
            listOf(validMovie)
        )
        viewModel.handleMovies(data)
        assertEquals(
            DiscoverMoviesUiState(movies = data, isLoading = false, isError = false),
            viewModel.discoverMoviesUiState.getOrAwaitValue()
        )
    }

    @Test
    fun `test DiscoverMoviesViewModel handleError sets the discoverMoviesUiState correctly`() {
        viewModel.handleError(Throwable())
        assertEquals(
            DiscoverMoviesUiState(movies = null, isLoading = false, isError = true),
            viewModel.discoverMoviesUiState.getOrAwaitValue()
        )
    }

}