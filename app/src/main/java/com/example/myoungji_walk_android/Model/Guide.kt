package com.example.myoungji_walk_android.Model

import java.io.Serializable

data class Guide(
    val distance: String,
    val pointIndex: Int,
    val type: String
) : Serializable
