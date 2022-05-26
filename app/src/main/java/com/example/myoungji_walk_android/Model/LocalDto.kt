package com.example.myoungji_walk_android.Model

import java.io.Serializable

data class LocalDto(
    val latitude: String,
    val longitude: String,
    val name: String
) : Serializable