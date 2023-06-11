package com.example.moviesapp.ui.details.di

import com.example.moviesapp.di.AppComponent
import com.example.moviesapp.ui.details.MovieDetailsFragment
import dagger.Component

@MovieDetailsFragmentScope
@Component(
    modules = [],
    dependencies = [AppComponent::class]
)
interface MovieDetailsFragmentComponent {

    fun inject(fragment: MovieDetailsFragment)
}