package com.example.myoungji_walk_android.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class nodeDto(
    //출발지, 목적지
    @SerializedName("id") val id: Int,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    //장소
    @SerializedName("name") val name: String
) : Serializable