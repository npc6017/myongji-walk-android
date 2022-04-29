package com.example.myoungji_walk_android.api

import com.example.myoungji_walk_android.Model.LocalDto
import com.example.myoungji_walk_android.Model.RouteDto
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

    //todo token, 출발지좌표, 목적지좌표, 옵션 추가해서 보내야함
    @GET("/v3/16825ad2-52da-423f-87f9-dc49952ee028")
    fun priviewRoute(): Call<RouteDto>
}