package com.example.moviesapp.ui.discover.di

import com.example.moviesapp.di.AppComponent
import com.example.moviesapp.ui.discover.DiscoverFragment
import dagger.Component

@DiscoverFragmentScope
@Component(
    modules = [DiscoverFragmentModule::class],
    dependencies = [AppComponent::class]
)
interface DiscoverFragmentComponent {
    fun inject(fragment: DiscoverFragment)
}