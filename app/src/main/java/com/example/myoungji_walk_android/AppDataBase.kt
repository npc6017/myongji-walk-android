package com.example.myoungji_walk_android

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myoungji_walk_android.Model.BookMark
import com.example.myoungji_walk_android.Model.History
import com.example.myoungji_walk_android.dao.BookMarkDao
import com.example.myoungji_walk_android.dao.HistoryDao

@Database(entities = [History::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao

}

@Database(entities = [BookMark::class], version = 1)
abstract class AppDataBaseBookMark: RoomDatabase() {
    abstract fun bookMarkDao(): BookMarkDao

}