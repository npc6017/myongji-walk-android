package com.example.myoungji_walk_android.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Guide(
    val pointIndex: Int,
    val type: Int,
    val distance: String
) : Serializable
