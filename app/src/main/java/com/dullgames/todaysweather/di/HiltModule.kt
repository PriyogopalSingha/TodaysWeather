package com.dullgames.todaysweather.di

import com.dullgames.todaysweather.BASE_URL
import com.dullgames.todaysweather.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module()
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun providesRetrofit() = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit) = retrofit.create(WeatherApi::class.java)
}