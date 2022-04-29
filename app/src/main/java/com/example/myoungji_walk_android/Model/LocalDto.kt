package com.example.myoungji_walk_android.Model

import com.example.m_work.Model.Local
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocalDto(
    val start: Int,
    val items: List<Local>
) : Serializable