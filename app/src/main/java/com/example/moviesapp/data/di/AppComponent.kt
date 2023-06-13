package com.example.moviesapp.data.di

import com.example.moviesapp.data.MoviesRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun inject(application: MoviesApplication)
    fun repository(): MoviesRepository
}