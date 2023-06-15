package com.example.moviesapp.data.repository.di

import com.example.moviesapp.data.repository.MoviesRepositoryImplTest
import dagger.Component


@TestMoviesRepositoryImplScope
@Component(modules = [TestMockkMoviesRepositoryImplModule::class])
interface TestMoviesRepositoryImplComponent {

    fun inject(moviesRepositoryImplTest: MoviesRepositoryImplTest)
}