package com.example.myoungji_walk_android.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookMark(
    @PrimaryKey val uid : Int?,
    @ColumnInfo(name = "keyword") val keyword : String?
)
