package com.example.moviesapp.data.repository.di

import com.example.moviesapp.data.network.MovieApi
import com.example.moviesapp.data.paging.GetMoviesRxPagingSource
import com.example.moviesapp.data.repository.MoviesRepositoryImpl
import dagger.Module
import dagger.Provides
import io.mockk.mockkClass
import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.TrampolineScheduler

@Module
class TestMockkMoviesRepositoryImplModule {

    @Provides
    @TestMoviesRepositoryImplScope
    fun bindMovieApi(): MovieApi =
        mockkClass(MovieApi::class)

    @Provides
    @TestMoviesRepositoryImplScope
    fun bindGetMoviesRxPagingSource(): GetMoviesRxPagingSource =
        mockkClass(GetMoviesRxPagingSource::class)

    @Provides
    @TestMoviesRepositoryImplScope
    fun bindTestScheduler(): Scheduler = TrampolineScheduler.instance()

    @Provides
    @TestMoviesRepositoryImplScope
    fun bindMoviesRepositoryImpl(
        movieApi: MovieApi,
        getMoviesRxPagingSource: GetMoviesRxPagingSource,
        testScheduler: Scheduler
    ): MoviesRepositoryImpl =
        MoviesRepositoryImpl(movieApi, getMoviesRxPagingSource, testScheduler, testScheduler)
}