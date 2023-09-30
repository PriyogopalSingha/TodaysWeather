package com.dullgames.todaysweather.repo

import com.dullgames.todaysweather.API_KEY
import com.dullgames.todaysweather.Resource
import com.dullgames.todaysweather.UNIT
import com.dullgames.todaysweather.models.CityWeather
import com.dullgames.todaysweather.remote.WeatherApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class WeatherRepoImpl @Inject constructor(val api: WeatherApi){
    private val _weatherDetailsFlow = MutableStateFlow<Resource<CityWeather>>(Resource.Nothing())
    val weatherDetailsFlow: StateFlow<Resource<CityWeather>>
        get() = _weatherDetailsFlow

    suspend fun getWeather(query: String){
        _weatherDetailsFlow.emit(Resource.Loading())
        val response = api.getWeatherInfo(query, API_KEY, UNIT)
        if(response.isSuccessful && response.body() != null){
            _weatherDetailsFlow.emit(Resource.Success(response.body()!!))
        }else if(!response.isSuccessful){
            _weatherDetailsFlow.emit(Resource.Error(message = "Something went wrong!"))
        }else if(response.body()!= null){
            _weatherDetailsFlow.emit(Resource.Error(message = "Something went wrong!"))
        }

    }
}