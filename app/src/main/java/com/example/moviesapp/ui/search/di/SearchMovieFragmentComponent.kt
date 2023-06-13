package com.example.moviesapp.ui.search.di

import com.example.moviesapp.data.di.AppComponent
import com.example.moviesapp.ui.search.SearchMovieFragment
import dagger.Component

@SearchMovieFragmentScope
@Component(
    modules = [SearchMovieFragmentModule::class],
    dependencies = [AppComponent::class]
)
interface SearchMovieFragmentComponent {
    fun inject(fragment: SearchMovieFragment)
}