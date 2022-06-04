package com.example.myoungji_walk_android.Model

import java.io.Serializable

data class pathFindDto(
    //도착지좌표
    val goal: nodeDto,
    //거리, 방향
    val guide: List<Guide>,
    //체크포인트
    val items: List<nodeDto>,
    //출발지좌표
    val start: nodeDto,
    //총 거리
    val sumDistance: String
) : Serializable
