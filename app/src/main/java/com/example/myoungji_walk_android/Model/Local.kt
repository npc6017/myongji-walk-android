package com.example.m_work.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Local(
    @SerializedName("title") val title: String,
    @SerializedName("address") val address: String,
    @SerializedName("mapx") val mapx: Int,
    @SerializedName("mapy") val mapy: Int
) : Serializable