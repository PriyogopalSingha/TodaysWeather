package com.dullgames.todaysweather.remote

import com.dullgames.todaysweather.models.CityWeather
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeatherInfo(
        @Query("q") query: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String
    ): Response<CityWeather>
}