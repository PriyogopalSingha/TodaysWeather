package com.dullgames.todaysweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dullgames.todaysweather.repo.WeatherRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(val weatherRepo: WeatherRepoImpl) : ViewModel() {

    val weatherFlow
        get() = weatherRepo.weatherDetailsFlow
    init {
        getWeatherDetails("Delhi")
    }
    fun getWeatherDetails(query: String){
        viewModelScope.launch {
            weatherRepo.getWeather(query)
        }
    }
}