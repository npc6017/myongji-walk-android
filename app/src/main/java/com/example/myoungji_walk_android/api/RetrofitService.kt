package com.example.myoungji_walk_android.api

import com.example.myoungji_walk_android.Model.LocalDto
import com.example.myoungji_walk_android.Model.RouteDto
import com.example.myoungji_walk_android.Model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Header

interface RetrofitService {
    //todo 추후에 서버통신
    @GET("/v3/7120375d-db91-4a43-8d32-c6c71681fea5")
    fun searchLocation(
//        @Query("place") place: String
    ): Call<LocalDto>

    //TODO 추후에 서버통신 -> token, 출발지좌표, 목적지좌표, 옵션 추가해서 보내야함
    @GET("/v3/ca5bf253-9638-4798-801b-2dd1fb6f13fe")
    fun priviewRoute(
//        @Query("start") start: Int,
//        @Query("destination") destination: Int,
//        @Query("option") option: Int
    ): Call<RouteDto>

    //이메일, 비밀번호 전송
    @GET("/v3/ec70c99a-919f-4e6a-bbef-84fbd63fadf4")
    fun loginService(
//        @Query("email") email: String,
//        @Query("password") password: String
    ): Call<User>

    //이메일, 비밀번호, 학년, 이름
    @GET("")
    fun joinService(
//        @Query("email") email: String,
//        @Query("password") password: String,
//        @Query("name") name: String,
//        @Query("grade") grade: Int
    ): Call<ResponseBody>
}