package com.example.moviesapp.ui.discover.di

import com.example.moviesapp.data.di.AppComponent
import com.example.moviesapp.ui.discover.DiscoverMoviesFragment
import dagger.Component

@DiscoverMoviesFragmentScope
@Component(
    modules = [DiscoverMoviesFragmentModule::class],
    dependencies = [AppComponent::class]
)
interface DiscoverMoviesFragmentComponent {
    fun inject(fragment: DiscoverMoviesFragment)
}