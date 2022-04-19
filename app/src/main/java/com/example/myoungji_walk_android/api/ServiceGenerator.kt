package com.example.myoungji_walk_android.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceGenerator {
    private val builder = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com")
        .addConverterFactory(GsonConverterFactory.create())
    private val retrofit = builder.build()

    fun <S> createService(serviceClass: Class<S>?): S {
        return retrofit.create(serviceClass)
    }
}