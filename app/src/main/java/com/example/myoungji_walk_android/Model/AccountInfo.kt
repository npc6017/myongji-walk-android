package com.example.myoungji_walk_android.Model

import com.google.gson.annotations.SerializedName

data class AccountInfo(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)
