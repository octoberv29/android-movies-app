package com.example.moviesapp.data.di

import android.app.Application

class MoviesApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule(applicationContext as Application))
            .build()
    }
}