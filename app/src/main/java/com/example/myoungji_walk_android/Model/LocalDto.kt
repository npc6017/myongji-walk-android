package com.example.myoungji_walk_android.Model

import com.example.m_work.Model.Local
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocalDto(
    @SerializedName("start") val start: Int,
    @SerializedName("items") val items: List<Local>
) : Serializable