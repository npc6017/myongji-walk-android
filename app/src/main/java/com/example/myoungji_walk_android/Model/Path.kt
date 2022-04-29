package com.example.myoungji_walk_android.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Path(
    val lat: Double,
    val lng: Double
) : Serializable
