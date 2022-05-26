package com.example.myoungji_walk_android.api

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.google.gson.internal.GsonBuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceGenerator {
    private const val BASE_URL = "http://118.67.129.128:8080/"
    private var gsonBuilder = GsonBuilder().setLenient().create()
    private val httpClient = OkHttpClient.Builder()
    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
    private var retrofit = builder.build()
    fun <S> createService(serviceClass: Class<S>?): S {
        return createService(serviceClass, null)
    }

    fun <S> createService(
        serviceClass: Class<S>?, accessToken: String?
    ): S {
        if (!TextUtils.isEmpty(accessToken)) {
            val interceptor = AuthenticationInterceptor(accessToken!!)
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor)
                builder.client(httpClient.build())
                retrofit = builder.build()
            }
        }
        return retrofit.create(serviceClass)
    }
}