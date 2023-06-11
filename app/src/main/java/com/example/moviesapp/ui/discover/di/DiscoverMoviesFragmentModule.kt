package com.example.moviesapp.ui.discover.di

import com.example.moviesapp.data.MoviesRepository
import com.example.moviesapp.ui.discover.DiscoverMoviesViewModel
import dagger.Module
import dagger.Provides

@Module
class DiscoverMoviesFragmentModule {

    @Provides
    @DiscoverMoviesFragmentScope
    fun provideDiscoverViewModelFactory(
        moviesRepository: MoviesRepository
    ): DiscoverMoviesViewModel.Companion.DiscoverMoviesViewModelFactory {
        return DiscoverMoviesViewModel.Companion.DiscoverMoviesViewModelFactory(moviesRepository)
    }
}