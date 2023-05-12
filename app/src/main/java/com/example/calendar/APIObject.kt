package com.example.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIObject {
    private var retrofit: Retrofit? = null
    var BASE_URL = "https://api.openweathermap.org/data/2.5"

    fun getAPIInterface():APIInterface?{
        if(retrofit==null){
            retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit?.create(APIInterface::class.java)
    }
}