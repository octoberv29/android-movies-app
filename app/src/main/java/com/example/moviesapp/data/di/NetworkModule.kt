package com.example.moviesapp.data.di

import android.app.Application
import com.example.moviesapp.data.ConstantsData
import com.example.moviesapp.data.ConstantsData.Companion.API_KEY
import com.example.moviesapp.data.ConstantsData.Companion.SAMPLE_API_KEY
import com.example.moviesapp.data.repository.MoviesRepository
import com.example.moviesapp.data.repository.MoviesRepositoryImpl
import com.example.moviesapp.data.network.MovieApi
import com.example.moviesapp.data.paging.GetMoviesRxPagingSource
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ConstantsData.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .addInterceptor(authInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->

            val url: HttpUrl = chain.request().url.newBuilder()
                    // in a production app this api key should come from the backend or be hidden
                    // but for the purpose of demonstrating the app functionality I left it here
                .addQueryParameter(API_KEY, SAMPLE_API_KEY)
                .build()

            val request: Request = chain.request().newBuilder()
                .url(url)
                .build()

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi =
        retrofit.create(MovieApi::class.java)

    @Provides
    @Singleton
    fun provideGetMoviesPagingSource(
        movieApi: MovieApi,
    ): GetMoviesRxPagingSource =
        GetMoviesRxPagingSource(movieApi)

    @Provides
    @Singleton
    fun provideRepository(
        movieApi: MovieApi,
        pagingSource: GetMoviesRxPagingSource
    ): MoviesRepository =
        MoviesRepositoryImpl(movieApi, pagingSource)
}