package com.example.myoungji_walk_android.Model

import java.io.Serializable

data class RouteDto(
    val start: Goal,
    val goal: Goal,
    val items: List<Path>,
    val guide: List<Guide>
) : Serializable
