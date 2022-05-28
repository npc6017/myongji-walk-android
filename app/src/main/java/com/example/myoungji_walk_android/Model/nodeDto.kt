package com.example.myoungji_walk_android.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class nodeDto(
    @SerializedName("id") val id: Int,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("name") val name: String
) : Serializable