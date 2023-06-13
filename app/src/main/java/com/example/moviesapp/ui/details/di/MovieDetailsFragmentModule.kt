package com.example.moviesapp.ui.details.di

import com.example.moviesapp.data.repository.MoviesRepository
import com.example.moviesapp.ui.details.MovieDetailsViewModel
import dagger.Module
import dagger.Provides

@Module
class MovieDetailsFragmentModule {

    @MovieDetailsFragmentScope
    @Provides
    fun provideMovieDetailsViewModelFactory(
        moviesRepository: MoviesRepository
    ): MovieDetailsViewModel.Companion.MovieDetailsViewModelFactory {
        return MovieDetailsViewModel.Companion.MovieDetailsViewModelFactory(moviesRepository)
    }

}