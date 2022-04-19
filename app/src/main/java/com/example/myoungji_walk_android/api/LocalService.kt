package com.example.myoungji_walk_android.api

import com.example.myoungji_walk_android.Model.LocalDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Header

interface LocalService {
    @GET("/v1/search/local.json")
    fun searchLocation(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") keyword: String
    ): Call<LocalDto>
}