package com.dullgames.todaysweather.models

data class CityWeather(
    val base: String = "",
    val clouds: Clouds = Clouds(0),
    val cod: Int = 0,
    val coord: Coord = Coord(.00,.00),
    val dt: Int = 0,
    val id: Int = 0,
    val main: Main = Main(),
    val name: String = "",
    val sys: Sys = Sys(),
    val timezone: Int = 0,
    val visibility: Int = 0,
    val weather: List<Weather> = emptyList(),
    val wind: Wind = Wind()
)