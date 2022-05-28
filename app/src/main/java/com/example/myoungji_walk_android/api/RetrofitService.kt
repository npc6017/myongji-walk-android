package com.example.myoungji_walk_android.api

import com.example.myoungji_walk_android.Model.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("/node")
    fun searchLocation(
        @Query("name") name: String
    ): Call<nodeDto>

    @GET("/map/pathFind")
    fun priviewRoute(
        @Query("start") start: Int,
        @Query("end") end: Int,
        @Query("weightCode") weightCode: String
    ): Call<pathFindDto>

    //이메일, 비밀번호 전송
    @POST("/account/signIn")
    fun loginService(
        @Body accountInfo: AccountInfo
    ): Call<Token>

    //이메일, 비밀번호
    @POST("/account")
    fun joinService(
        @Body accountInfo: AccountInfo
    ): Call<String>

    @POST("/account/valid")
    fun certificationStudent(
        @Body accountEmail: AccountEmail
    ): Call<String>

    @POST("/account/code")
    fun certificationNumber(
        @Body accountCode: AccountCode
    ): Call<String>
}