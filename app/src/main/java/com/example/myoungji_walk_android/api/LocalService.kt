package com.example.myoungji_walk_android.api

import com.example.myoungji_walk_android.Model.LocalDto
import com.example.myoungji_walk_android.Model.RouteDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Header

interface LocalService {
    //todo token, 출발지이름, 목적지이름 추가 후 전송
    @GET("/v3/7120375d-db91-4a43-8d32-c6c71681fea5")
    fun searchLocation(): Call<LocalDto>

    //todo token, 출발지좌표, 목적지좌표, 옵션 추가해서 보내야함
    @GET("/v3/ca5bf253-9638-4798-801b-2dd1fb6f13fe")
    fun priviewRoute(): Call<RouteDto>

    @GET("/v3/918f3dfc-282c-47b6-87a0-e107f24d92c7")
    fun loginService(): Call<ResponseBody>

    @GET("")
    fun joinService(): Call<ResponseBody>
}