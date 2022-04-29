package com.example.m_work.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Local(
    val title: String,
    val address: String,
    val mapx: Int,
    val mapy: Int
) : Serializable