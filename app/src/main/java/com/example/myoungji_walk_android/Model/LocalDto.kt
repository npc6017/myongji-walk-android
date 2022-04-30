package com.example.myoungji_walk_android.Model

import java.io.Serializable

data class LocalDto(
    val place: String,
    val location: List<Double>
) : Serializable