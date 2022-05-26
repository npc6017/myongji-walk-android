package com.example.myoungji_walk_android.Model

import com.google.gson.annotations.SerializedName

data class AccountCode(
    @SerializedName("code") val code: Int,
    @SerializedName("email") val email: String
)
