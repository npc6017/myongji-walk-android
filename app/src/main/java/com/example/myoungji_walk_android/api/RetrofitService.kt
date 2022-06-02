package com.example.myoungji_walk_android.api

import com.example.myoungji_walk_android.Model.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    //@GET("v3/dea07e2b-35b8-4a39-9ef1-fd97961b71d6")
    @GET("/node")
    fun searchLocation(
        @Header("accessToken") accessToken: String,
        @Query("name") name: String
    ): Call<nodeDto>

    //@GET("/v3/767f7d0f-c9b7-49e1-a258-701c0e5705e1")
    @GET("/map/pathFind")
    fun priviewRoute(
        @Header("accessToken") accessToken: String,
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

    //인증코드 요청
    @POST("/account/valid")
    fun certificationStudent(
        @Body accountEmail: AccountEmail
    ): Call<String>

    @POST("/account/code")
    fun certificationNumber(
        @Body accountCode: AccountCode
    ): Call<String>
}