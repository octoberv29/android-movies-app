package com.example.moviesapp.ui.discover.di

import com.example.moviesapp.data.MoviesRepository
import com.example.moviesapp.ui.discover.DiscoverMoviesViewModel
import dagger.Module
import dagger.Provides

@Module
class DiscoverFragmentModule {

    @Provides
    @DiscoverFragmentScope
    fun provideDiscoverViewModelFactory(
        moviesRepository: MoviesRepository
    ): DiscoverMoviesViewModel.Companion.DiscoverViewModelFactory {
        return DiscoverMoviesViewModel.Companion.DiscoverViewModelFactory(moviesRepository)
    }
}