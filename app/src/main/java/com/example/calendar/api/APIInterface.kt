package com.example.calendar.api

import com.example.calendar.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {

    @GET("weather")
    fun getCurrentWeatherData(
        @Query("lat") lat:String,
        @Query("lon")lon:String,
        @Query("APPID") appID:String
    ): Call<WeatherModel>

    @GET("weather")
    fun getCityWeatherData(
        @Query("q") q:String,
        @Query("APPID") appID:String
    ): Call<WeatherModel>

}