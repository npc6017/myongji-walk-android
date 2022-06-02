package com.example.myoungji_walk_android.Model

import java.io.Serializable

data class pathFindDto(
    val goal: nodeDto,
    val guide: List<Guide>,
    val items: List<nodeDto>,
    val start: nodeDto,
    val sumDistance: String
) : Serializable
