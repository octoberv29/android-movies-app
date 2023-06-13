package com.example.moviesapp.ui.search.di

import com.example.moviesapp.data.repository.MoviesRepository
import com.example.moviesapp.ui.search.SearchMovieViewModel
import dagger.Module
import dagger.Provides

@Module
class SearchMovieFragmentModule {

    @Provides
    @SearchMovieFragmentScope
    fun provideSearchMovieViewModelFactory(
        moviesRepository: MoviesRepository
    ): SearchMovieViewModel.Companion.SearchMovieViewModelFactory {
        return SearchMovieViewModel.Companion.SearchMovieViewModelFactory(moviesRepository)
    }
}